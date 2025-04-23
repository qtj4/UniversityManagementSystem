package com.qtj4.ums.controller;

import com.qtj4.ums.dto.UserDTO;
import com.qtj4.ums.model.Role;
import com.qtj4.ums.service.UserService;
import com.qtj4.ums.exception.NotFoundException;
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
@RequestMapping("/students")
@Tag(name = "Student Management", description = "Endpoints for managing student users")
@SecurityRequirement(name = "BearerAuth")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all students", description = "Retrieves a list of all students (ADMIN only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of students retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<UserDTO>> getAllStudents() {
        logger.info("Fetching all students");
        return ResponseEntity.ok(userService.getUsersByRole(Role.STUDENT));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new student", description = "Creates a new student user (ADMIN only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<UserDTO> createStudent(@Valid @RequestBody UserDTO userDTO) {
        logger.info("Creating new student: {}", userDTO.getUsername());
        userDTO.setRole(Role.STUDENT);
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get a student by ID", description = "Retrieves a student by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<UserDTO> getStudentById(@PathVariable Long id) {
        logger.info("Fetching student with ID: {}", id);
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO.getRole() != Role.STUDENT) {
            logger.warn("Requested ID {} is not a student", id);
            throw new NotFoundException("Student not found");
        }
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a student", description = "Updates an existing student by ID (ADMIN only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<UserDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        logger.info("Updating student with ID: {}", id);
        userDTO.setRole(Role.STUDENT);
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a student", description = "Deletes a student by ID (ADMIN only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        logger.info("Deleting student with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}