package com.qtj4.ums.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EnrollmentDTO {
    private Long id;

    @NotNull(message = "Student ID is mandatory")
    private Long studentId;

    @NotNull(message = "Course ID is mandatory")
    private Long courseId;

    private LocalDate enrollmentDate;
    private String grade;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}