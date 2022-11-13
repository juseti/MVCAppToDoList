package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.security.CustomUserDetails;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;



    @Test
    @Transactional
    public void getAllUsersTests() throws Exception {
        List<User> expected = userService.getAll();

        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/all")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.model().attribute("users",expected));
    }


    @Test
    @Transactional
    public void addNewUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("email","new@user.com")
                .param("firstName","New")
                .param("lastName","User")
                .param("password","password123"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }


    @Test
    @Transactional
    public void addNewUserWithInvalidNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("email","new@user.com")
                        .param("firstName","")
                        .param("lastName","User")
                        .param("password","password123"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @Transactional
    public void createUserGetMethod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }


    @Test
    @Transactional
    public void addInvalidUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }


    @Test
    @Transactional
    public void readUserTest() throws Exception{
        User expected = userService.readById(5L);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/"+expected.getId()+"/read")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.model().attribute("user",expected));
    }


    @Test
    @Transactional
    public void readNotExistUserTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/users/"+12L+"/read")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    @Test
    @Transactional
    public void updateExistUser() throws Exception {
        User expected = userService.readById(5L);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/"+expected.getId()+"/update")
                        .param("email","new@user.com")
                        .param("firstName","New")
                        .param("lastName","User")
                        .param("password","password123")
                        .param("roleId","2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }


    @Test
    @Transactional
    public void updateExistUserWithInvalidName() throws Exception {
        User expected = userService.readById(5L);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/"+expected.getId()+"/update")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("email","new@user.com")
                        .param("firstName","")
                        .param("lastName","User")
                        .param("password","password123")
                        .param("roleId","2"))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }


    @Test
    @Transactional
    public void updateUserTest() throws Exception {
        List<Role> expectedRoles = roleService.getAll();
        User user = userService.readById(5);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/update")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user", "roles"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user))
                .andExpect(MockMvcResultMatchers.model().attribute("roles", expectedRoles));
    }


    @Test
    @Transactional
    public void updateNotExistUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/"+11L+"/update")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("email","new@user.com")
                        .param("firstName","New")
                        .param("lastName","User")
                        .param("password","password123")
                        .param("roleId","21"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @Test
    @Transactional
    public void deleteUser() throws Exception {
        User expected = userService.readById(5L);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/"+expected.getId()+"/delete")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }


    @Test
    @Transactional
    public void deleteNotExistUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/"+20L+"/delete")
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                CustomUserDetails.getUserDetails(userService.readById(4L)))
                        )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }
}
