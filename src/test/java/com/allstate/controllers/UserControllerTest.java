package com.allstate.controllers;

import com.allstate.entities.User;
import com.allstate.services.UserService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldCreateUser() throws Exception {
        // stub
        User user = new User();
        user.setId(1);
        user.setEmail("alice@gmail.com");
        when(this.service.create("alice@gmail.com")).thenReturn(user);

        // request
        MockHttpServletRequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"alice@gmail.com\"}");

        // assertion
        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("alice@gmail.com")));
    }

    @Test(expected = org.springframework.web.util.NestedServletException.class)
    public void shouldNotCreateUserEmptyEmail() throws Exception {
        // stub
        when(this.service.create(anyString())).thenThrow(new IllegalArgumentException("Email is Required"));

        // request
        MockHttpServletRequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"\"}");

        // assertion
        this.mvc.perform(request);
    }

    @Test(expected = org.springframework.web.util.NestedServletException.class)
    public void shouldNotCreateUserNoEmailKey() throws Exception {
        // stub
        when(this.service.create(anyString())).thenThrow(new IllegalArgumentException("Email is Required"));

        // request
        MockHttpServletRequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");

        // assertion
        this.mvc.perform(request);
    }

    @Test(expected = org.springframework.web.util.NestedServletException.class)
    public void shouldNotCreateUserDuplicateEmail() throws Exception {
        // stub
        when(this.service.create(anyString())).thenThrow(new IllegalArgumentException("Duplicate Email"));

        // request
        MockHttpServletRequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"alice@gmail.com\"}");

        // assertion
        this.mvc.perform(request);
    }
}