package com.qtj4.ums.controller;

import com.qtj4.ums.dto.CourseDTO;
import com.qtj4.ums.service.CourseService;
import com.qtj4.ums.strategy.SortByStartDate;
import com.qtj4.ums.strategy.SortByNumberOfStudents;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@Tag(name = "Course Management", description = "Endpoints for managing courses")
@SecurityRequirement(name = "BearerAuth")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all courses", description = "Retrieves a list of all courses, optionally sorted by start date or number of students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of courses retrieved successfully")
    })
    public ResponseEntity<List<CourseDTO>> getAllCourses(
            @Parameter(description = "Sort by 'startDate' or 'numberOfStudents'") @RequestParam(required = false) String sortBy) {
        logger.info("Fetching all courses with sortBy: {}", sortBy);
        if ("startDate".equals(sortBy)) {
            return ResponseEntity.ok(courseService.getAllCoursesSorted(new SortByStartDate()));
        } else if ("numberOfStudents".equals(sortBy)) {
            return ResponseEntity.ok(courseService.getAllCoursesSorted(new SortByNumberOfStudents()));
        } else {
            return ResponseEntity.ok(courseService.getAllCoursesSorted(courses -> {}));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create a new course", description = "Creates a new course (ADMIN or TEACHER only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        logger.info("Creating new course: {}", courseDTO.getName());
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get a course by ID", description = "Retrieves a course by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        logger.info("Fetching course with ID: {}", id);
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update a course", description = "Updates an existing course by ID (ADMIN or TEACHER only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO courseDTO) {
        logger.info("Updating course with ID: {}", id);
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Delete a course", description = "Deletes a course by ID (ADMIN or TEACHER only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        logger.info("Deleting course with ID: {}", id);
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}