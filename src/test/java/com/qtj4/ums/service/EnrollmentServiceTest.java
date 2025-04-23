package com.qtj4.ums.service;

import com.qtj4.ums.dto.EnrollmentDTO;
import com.qtj4.ums.model.Enrollment;
import com.qtj4.ums.model.User;
import com.qtj4.ums.model.Course;
import com.qtj4.ums.repository.EnrollmentRepository;
import com.qtj4.ums.repository.UserRepository;
import com.qtj4.ums.repository.CourseRepository;
import com.qtj4.ums.exception.NotFoundException;
import com.qtj4.ums.mapper.EnrollmentMapper;
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
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;
    private EnrollmentDTO enrollmentDTO;
    private User student;
    private Course course;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setUsername("student");

        course = new Course();
        course.setId(1L);
        course.setName("Mathematics");

        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());

        enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setId(1L);
        enrollmentDTO.setStudentId(1L);
        enrollmentDTO.setCourseId(1L);
        enrollmentDTO.setEnrollmentDate(LocalDate.now());
    }

    @Test
    void getAllEnrollments_success() {
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList(enrollment));
        when(enrollmentMapper.toDto(enrollment)).thenReturn(enrollmentDTO);

        var result = enrollmentService.getAllEnrollments();

        assertEquals(1, result.size());
        assertEquals(enrollmentDTO, result.get(0));
        verify(enrollmentRepository).findAll();
        verify(enrollmentMapper).toDto(enrollment);
    }

    @Test
    void getEnrollmentById_success() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentMapper.toDto(enrollment)).thenReturn(enrollmentDTO);

        EnrollmentDTO result = enrollmentService.getEnrollmentById(1L);

        assertEquals(enrollmentDTO, result);
        verify(enrollmentRepository).findById(1L);
        verify(enrollmentMapper).toDto(enrollment);
    }

    @Test
    void getEnrollmentById_notFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> enrollmentService.getEnrollmentById(1L));
        verify(enrollmentRepository).findById(1L);
    }

    @Test
    void createEnrollment_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentMapper.toEntity(enrollmentDTO)).thenReturn(enrollment);
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);
        when(enrollmentMapper.toDto(enrollment)).thenReturn(enrollmentDTO);

        EnrollmentDTO result = enrollmentService.createEnrollment(enrollmentDTO);

        assertEquals(enrollmentDTO, result);
        verify(userRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(enrollmentMapper).toEntity(enrollmentDTO);
        verify(enrollmentRepository).save(enrollment);
        verify(enrollmentMapper).toDto(enrollment);
    }

    @Test
    void createEnrollment_studentNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> enrollmentService.createEnrollment(enrollmentDTO));
        verify(userRepository).findById(1L);
    }
}