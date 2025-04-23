package com.qtj4.ums.controller;

import com.qtj4.ums.dto.EnrollmentDTO;
import com.qtj4.ums.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    private EnrollmentDTO enrollmentDTO;

    @BeforeEach
    void setUp() {
        enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setId(1L);
        enrollmentDTO.setStudentId(1L);
        enrollmentDTO.setCourseId(1L);
        enrollmentDTO.setEnrollmentDate(LocalDate.now());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllEnrollments_success() throws Exception {
        when(enrollmentService.getAllEnrollments()).thenReturn(Arrays.asList(enrollmentDTO));

        mockMvc.perform(get("/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(1L));

        verify(enrollmentService).getAllEnrollments();
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void createEnrollment_success() throws Exception {
        when(enrollmentService.createEnrollment(any(EnrollmentDTO.class))).thenReturn(enrollmentDTO);

        mockMvc.perform(post("/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\":1,\"courseId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1L));

        verify(enrollmentService).createEnrollment(any(EnrollmentDTO.class));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void getEnrollmentById_success() throws Exception {
        when(enrollmentService.getEnrollmentById(1L)).thenReturn(enrollmentDTO);

        mockMvc.perform(get("/enrollments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1L));

        verify(enrollmentService).getEnrollmentById(1L);
    }
}