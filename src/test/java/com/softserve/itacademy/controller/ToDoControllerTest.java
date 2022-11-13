package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.security.CustomUserDetails;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoService todoService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void createToDoGetMethodTest() throws Exception {
        long ownerId = userService.getAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/create/users/" + ownerId)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo", "ownerId"))
                .andExpect(MockMvcResultMatchers.model().attribute("ownerId", ownerId));
    }


    @Test
    @Transactional
    public void createToDoPostMethodTest() throws Exception {
        long ownerId = userService.getAll().get(0).getId();
        int expected = 5;

        mockMvc.perform(MockMvcRequestBuilders.post("/todos/create/users/" + ownerId)
                    .with(SecurityMockMvcRequestPostProcessors.user(
                            CustomUserDetails.getUserDetails(userService.readById(4L)))
                    )
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .param("title", "My ToDo"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        int actual = todoService.getByUserId(ownerId).size();

        assertEquals(expected, actual);
    }


    @Test
    @Transactional
    public void createFailToDoPostMethodTest() throws Exception {
        long ownerId = 100L;
        mockMvc.perform(MockMvcRequestBuilders.post("/todos/create/users/" + ownerId)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("title", "My ToDo"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    @Transactional
    public void createInvalidToDoPostMethodTest() throws Exception {
        long ownerId = userService.getAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/todos/create/users/" + ownerId)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("title", ""))
                .andExpect(MockMvcResultMatchers.view().name("create-todo"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @Transactional
    public void readToDoGetMethodTest() throws Exception {
        ToDo toDo = todoService.getAll().get(0);
        List<Task> tasks = taskService.getByTodoId(toDo.getId());
        List<User> users = userService.getAll().stream()
                .filter(user -> user.getId() != toDo.getOwner().getId())
                .collect(Collectors.toList());

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + toDo.getId() + "/tasks")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo", "tasks", "users"))
                .andExpect(MockMvcResultMatchers.model().attribute("todo", toDo))
                .andExpect(MockMvcResultMatchers.model().attribute("tasks",tasks))
                .andExpect(MockMvcResultMatchers.model().attribute("users", users))
                .andExpect(MockMvcResultMatchers.view().name("todo-tasks"));
    }


    @Test
    @Transactional
    public void readNotExistToDoGetMethodTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + 100L + "/tasks")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    @Transactional
    public void updateToDoGetMethodTest() throws Exception {
        ToDo toDo = todoService.getAll().get(0);
        long todoId = toDo.getId();
        long ownerId = toDo.getOwner().getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + todoId + "/update/users/" + ownerId)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo"))
                .andExpect(MockMvcResultMatchers.model().attribute("todo", toDo))
                .andExpect(MockMvcResultMatchers.view().name("update-todo"));
    }


    @Test
    @Transactional
    public void updateToDoPostMethodTest() throws Exception {
        ToDo toDo = todoService.getAll().get(0);
        long todoId = toDo.getId();
        long ownerId = toDo.getOwner().getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/todos/" + todoId + "/update/users/" + ownerId)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("id", String.valueOf(todoId))
                        .param("title", "My ToDo"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        assertEquals(toDo.getTitle(), "My ToDo");
    }


    @Test
    @Transactional
    public void updateInvalidToDoPostMethodTest() throws Exception {
        ToDo toDo = todoService.getAll().get(0);
        long todoId = toDo.getId();
        long ownerId = toDo.getOwner().getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/todos/" + todoId + "/update/users/" + ownerId)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("id", String.valueOf(todoId))
                        .param("title", ""))
                .andExpect(MockMvcResultMatchers.view().name("update-todo"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @Transactional
    public void updateNotExistingToDoPostMethodTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/todos/" + 100L + "/update/users/" + 100L)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("id", String.valueOf(100L))
                        .param("title", ""))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    @Transactional
    public void deleteToDoGetMethodTest() throws Exception {
        ToDo toDo = todoService.getAll().get(0);
        long todoId = toDo.getId();
        long ownerId = toDo.getOwner().getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + todoId + "/delete/users/" + ownerId)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        assertFalse(todoService.getAll().contains(toDo));
        assertFalse(userService.readById(ownerId).getMyTodos().contains(toDo));
    }


    @Test
    @Transactional
    public void deleteNotExistingToDoGetMethodTest() throws Exception {
        long ownerId = userService.getAll().get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + 100L + "/delete/users/" + ownerId)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }


    @Test
    @Transactional
    public void getAllToDoGetMethodTest() throws Exception {
        long userId = 4L;
        User user = userService.readById(userId);
        List<ToDo> todos = todoService.getByUserId(userId);


        mockMvc.perform(MockMvcRequestBuilders.get("/todos/all/users/" + userId)
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todos", "user"))
                .andExpect(MockMvcResultMatchers.model().attribute("todos", todos))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user))
                .andExpect(MockMvcResultMatchers.view().name("todos-user"));

        assertEquals(3, user.getMyTodos().size());
    }



    @Test
    @Transactional
    public void addCollaboratorToDoGetMethodTest() throws Exception {
        ToDo toDo = userService.getAll().get(0).getMyTodos().get(0);
        long userId = userService.getAll().get(1).getId();
        int expected = toDo.getCollaborators().size() + 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/"+ toDo.getId() + "/add/")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("user_id", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        assertEquals(expected, toDo.getCollaborators().size());
    }


    @Test
    @Transactional
    public void addNotExistingCollaboratorToDoGetMethodTest() throws Exception {
        ToDo toDo = userService.getAll().get(0).getMyTodos().get(0);
        long userId = 100L;

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/"+ toDo.getId() + "/add/")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("user_id", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }


    @Test
    @Transactional
    public void removeCollaboratorToDoGetMethodTest() throws Exception {
        ToDo toDo = userService.getAll().get(0).getMyTodos().get(0);
        long userId = userService.getAll().get(1).getId();
        int expected = toDo.getCollaborators().size() - 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/"+ toDo.getId() + "/remove/")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("user_id", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        assertEquals(expected, toDo.getCollaborators().size());
    }


    @Test
    @Transactional
    public void removeNotExistingCollaboratorToDoGetMethodTest() throws Exception {
        ToDo toDo = userService.getAll().get(0).getMyTodos().get(0);
        long userId = 100L;

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/"+ toDo.getId() + "/remove/")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("user_id", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}
