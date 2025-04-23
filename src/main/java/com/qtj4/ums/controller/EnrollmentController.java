package com.qtj4.ums.controller;

import com.qtj4.ums.dto.EnrollmentDTO;
import com.qtj4.ums.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/enrollments")
@Tag(name = "Enrollment Management", description = "Endpoints for managing course enrollments")
@SecurityRequirement(name = "BearerAuth")
public class EnrollmentController {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get all enrollments", description = "Retrieves a list of all enrollments (ADMIN or TEACHER only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of enrollments retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        logger.info("Fetching all enrollments");
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create a new enrollment", description = "Enrolls a student in a course (ADMIN or TEACHER only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EnrollmentDTO> createEnrollment(@Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        logger.info("Creating new enrollment for student ID: {}, course ID: {}", enrollmentDTO.getStudentId(), enrollmentDTO.getCourseId());
        return ResponseEntity.ok(enrollmentService.createEnrollment(enrollmentDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get an enrollment by ID", description = "Retrieves an enrollment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
        logger.info("Fetching enrollment with ID: {}", id);
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update an enrollment", description = "Updates an existing enrollment by ID (ADMIN or TEACHER only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<EnrollmentDTO> updateEnrollment(@PathVariable Long id, @Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        logger.info("Updating enrollment with ID: {}", id);
        return ResponseEntity.ok(enrollmentService.updateEnrollment(id, enrollmentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Delete an enrollment", description = "Deletes an enrollment by ID (ADMIN or TEACHER only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Enrollment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        logger.info("Deleting enrollment with ID: {}", id);
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}