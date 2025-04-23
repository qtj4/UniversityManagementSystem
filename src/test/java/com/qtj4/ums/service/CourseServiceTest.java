package com.qtj4.ums.service;

import com.qtj4.ums.dto.CourseDTO;
import com.qtj4.ums.model.Course;
import com.qtj4.ums.model.User;
import com.qtj4.ums.repository.CourseRepository;
import com.qtj4.ums.repository.UserRepository;
import com.qtj4.ums.exception.NotFoundException;
import com.qtj4.ums.mapper.CourseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private CourseDTO courseDTO;
    private User teacher;

    @BeforeEach
    void setUp() {
        teacher = new User();
        teacher.setId(1L);
        teacher.setUsername("teacher");

        course = new Course();
        course.setId(1L);
        course.setName("Mathematics");
        course.setTeacher(teacher);
        course.setStartDate(LocalDate.now());

        courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setName("Mathematics");
        courseDTO.setTeacherId(1L);
        courseDTO.setStartDate(LocalDate.now());
    }

    @Test
    void getAllCoursesSorted_success() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        var result = courseService.getAllCoursesSorted(courses -> {});

        assertEquals(1, result.size());
        assertEquals(courseDTO, result.get(0));
        verify(courseRepository).findAll();
        verify(courseMapper).toDto(course);
    }

    @Test
    void getCourseById_success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        CourseDTO result = courseService.getCourseById(1L);

        assertEquals(courseDTO, result);
        verify(courseRepository).findById(1L);
        verify(courseMapper).toDto(course);
    }

    @Test
    void getCourseById_notFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> courseService.getCourseById(1L));
        verify(courseRepository).findById(1L);
    }

    @Test
    void createCourse_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseMapper.toEntity(courseDTO)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        CourseDTO result = courseService.createCourse(courseDTO);

        assertEquals(courseDTO, result);
        verify(userRepository).findById(1L);
        verify(courseMapper).toEntity(courseDTO);
        verify(courseRepository).save(course);
        verify(courseMapper).toDto(course);
    }

    @Test
    void createCourse_teacherNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> courseService.createCourse(courseDTO));
        verify(userRepository).findById(1L);
    }
}