package com.qtj4.ums.strategy;

import com.qtj4.ums.model.Course;

import java.util.List;

public interface CourseSortingStrategy {
    void sort(List<Course> courses);
}