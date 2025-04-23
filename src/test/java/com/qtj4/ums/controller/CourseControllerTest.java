package com.qtj4.ums.controller;

import com.qtj4.ums.dto.CourseDTO;
import com.qtj4.ums.service.CourseService;
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

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setName("Mathematics");
        courseDTO.setTeacherId(1L);
        courseDTO.setStartDate(LocalDate.now());
    }

    @Test
    void getAllCourses_noSort() throws Exception {
        when(courseService.getAllCoursesSorted(any())).thenReturn(Arrays.asList(courseDTO));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mathematics"));

        verify(courseService).getAllCoursesSorted(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCourse_success() throws Exception {
        when(courseService.createCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Mathematics\",\"teacherId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mathematics"));

        verify(courseService).createCourse(any(CourseDTO.class));
    }

    @Test
    @WithMockUser
    void getCourseById_success() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(courseDTO);

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mathematics"));

        verify(courseService).getCourseById(1L);
    }
}