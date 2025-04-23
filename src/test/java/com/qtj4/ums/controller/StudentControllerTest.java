package com.qtj4.ums.controller;

import com.qtj4.ums.dto.UserDTO;
import com.qtj4.ums.model.Role;
import com.qtj4.ums.service.UserService;
import com.qtj4.ums.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setName("Test User");
        userDTO.setEmail("test@example.com");
        userDTO.setRole(Role.STUDENT);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllStudents_success() throws Exception {
        when(userService.getUsersByRole(Role.STUDENT)).thenReturn(Arrays.asList(userDTO));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"));

        verify(userService).getUsersByRole(Role.STUDENT);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createStudent_success() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"name\":\"Test User\",\"email\":\"test@example.com\",\"role\":\"STUDENT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser
    void getStudentById_success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService).getUserById(1L);
    }

    @Test
    @WithMockUser
    void getStudentById_notStudent() throws Exception {
        userDTO.setRole(Role.TEACHER);
        when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found"));

        verify(userService).getUserById(1L);
    }
}