package com.qtj4.ums.strategy;

import com.qtj4.ums.model.Course;

import java.util.List;

public class SortByNumberOfStudents implements CourseSortingStrategy {
    @Override
    public void sort(List<Course> courses) {
        courses.sort(Comparator.comparingInt(course -> course.getEnrollments().size()));
    }
}