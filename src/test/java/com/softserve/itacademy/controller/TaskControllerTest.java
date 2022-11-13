package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.security.CustomUserDetails;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UserService userService;

    TaskService taskService;
    ToDoService todoService;
    StateService stateService;
    public static Task validTask;

    @Autowired
    public TaskControllerTest(TaskService taskService,ToDoService todoService,StateService stateService, UserService userService) {
        this.taskService=taskService;
        this.todoService=todoService;
        this.stateService=stateService;
        this.userService=userService;
    }

    @BeforeAll
    public static void init(){
        validTask=new Task();
        validTask.setName("Valid Task");
        validTask.setPriority(Priority.MEDIUM);
    }


    @Test
    @Transactional
    public void testCreateValid_GET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/create/todos/7")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("task"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("priorities"))
                .andExpect(MockMvcResultMatchers.model().attribute("todo",todoService.readById(7L)))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities",Priority.values()));

    }


    @Test
    @Transactional
    public void testCreateValid_POST() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/create/todos/7")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name",validTask.getName())
                        .param("priority",validTask.getPriority().toString())
                        .param("todoId",String.valueOf(7)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }


    @Test
    @Transactional
    public void testCreateInvalidName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/create/todos/7")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name","")
                        .param("priority",validTask.getPriority().toString())
                        .param("todoId",String.valueOf(7)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("priorities"))
                .andExpect(MockMvcResultMatchers.model().attribute("todo",todoService.readById(7L)))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities",Priority.values()));
    }


    @Test
    @Transactional
    public void testCreateInvalidToDoId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/create/todos/15")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name","")
                        .param("priority",validTask.getPriority().toString())
                        .param("todoId",String.valueOf(15)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    @Transactional
    public void testUpdateValid_GET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/5/update/todos/7")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("task"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("states"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("priorities"))
                .andExpect(MockMvcResultMatchers.model().attribute("states",stateService.getAll()))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities",Priority.values()));

    }


    @Test
    @Transactional
    public void testUpdateValid_POST() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/5/update/todos/7")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("id",String.valueOf(5))
                        .param("name","New Task")
                        .param("priority",validTask.getPriority().toString())
                        .param("todoId",String.valueOf(7))
                        .param("stateId",String.valueOf(7)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }


    @Test
    @Transactional
    public void testUpdateInvalidName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/5/update/todos/7")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name","")
                        .param("priority",validTask.getPriority().toString())
                        .param("todoId",String.valueOf(7))
                        .param("stateId",String.valueOf(7)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @Transactional
    public void testUpdateInvalidToDoId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/5/update/todos/15")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name","")
                        .param("priority",validTask.getPriority().toString())
                        .param("todoId",String.valueOf(15))
                        .param("stateId",String.valueOf(7)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("states"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("priorities"))
                .andExpect(MockMvcResultMatchers.model().attribute("states",stateService.getAll()))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities",Priority.values()));
    }


    @Test
    @Transactional
    public void testUpdateInvalidStateId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/5/update/todos/7")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name","")
                        .param("priority",validTask.getPriority().toString())
                        .param("todoId",String.valueOf(7))
                        .param("stateId",String.valueOf(15)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("states"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("priorities"))
                .andExpect(MockMvcResultMatchers.model().attribute("states",stateService.getAll()))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities",Priority.values()));
    }


    @Test
    @Transactional
    public void testDeleteValidTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/6/delete/todos/7")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }


    @Test
    @Transactional
    public void testDeleteInvalidTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/30/delete/todos/7")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}