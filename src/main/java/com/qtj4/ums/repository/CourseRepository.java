package com.qtj4.ums.repository;

import com.qtj4.ums.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}