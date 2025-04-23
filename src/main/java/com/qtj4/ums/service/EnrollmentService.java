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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    public List<EnrollmentDTO> getAllEnrollments() {
        logger.debug("Fetching all enrollments");
        return enrollmentRepository.findAll().stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public EnrollmentDTO getEnrollmentById(Long id) {
        logger.debug("Fetching enrollment with ID: {}", id);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found with ID: " + id));
        return enrollmentMapper.toDto(enrollment);
    }

    public EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO) {
        logger.debug("Creating enrollment for student ID: {}, course ID: {}", enrollmentDTO.getStudentId(), enrollmentDTO.getCourseId());
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDTO);
        User student = userRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + enrollmentDTO.getStudentId()));
        Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + enrollmentDTO.getCourseId()));
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(enrollment);
    }

    public EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO) {
        logger.debug("Updating enrollment with ID: {}", id);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found with ID: " + id));
        User student = userRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found with ID: " + enrollmentDTO.getStudentId()));
        Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + enrollmentDTO.getCourseId()));
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(enrollmentDTO.getEnrollmentDate());
        enrollment.setGrade(enrollmentDTO.getGrade());
        enrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(enrollment);
    }

    public void deleteEnrollment(Long id) {
        logger.debug("Deleting enrollment with ID: {}", id);
        if (!enrollmentRepository.existsById(id)) {
            throw new NotFoundException("Enrollment not found with ID: " + id);
        }
        enrollmentRepository.deleteById(id);
    }
}