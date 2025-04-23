package com.qtj4.ums.service;

import com.qtj4.ums.dto.CourseDTO;
import com.qtj4.ums.model.Course;
import com.qtj4.ums.model.User;
import com.qtj4.ums.repository.CourseRepository;
import com.qtj4.ums.repository.UserRepository;
import com.qtj4.ums.exception.NotFoundException;
import com.qtj4.ums.mapper.CourseMapper;
import com.qtj4.ums.strategy.CourseSortingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseMapper courseMapper;

    public List<CourseDTO> getAllCoursesSorted(CourseSortingStrategy strategy) {
        logger.debug("Fetching all courses with sorting strategy");
        List<Course> courses = courseRepository.findAll();
        strategy.sort(courses);
        return courses.stream().map(courseMapper::toDto).collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long id) {
        logger.debug("Fetching course with ID: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + id));
        return courseMapper.toDto(course);
    }

    public CourseDTO createCourse(CourseDTO courseDTO) {
        logger.debug("Creating course: {}", courseDTO.getName());
        Course course = courseMapper.toEntity(courseDTO);
        User teacher = userRepository.findById(courseDTO.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found with ID: " + courseDTO.getTeacherId()));
        course.setTeacher(teacher);
        course = courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        logger.debug("Updating course with ID: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + id));
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        User teacher = userRepository.findById(courseDTO.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found with ID: " + courseDTO.getTeacherId()));
        course.setTeacher(teacher);
        course.setStartDate(courseDTO.getStartDate());
        course.setEndDate(courseDTO.getEndDate());
        course = courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    public void deleteCourse(Long id) {
        logger.debug("Deleting course with ID: {}", id);
        if (!courseRepository.existsById(id)) {
            throw new NotFoundException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
    }
}