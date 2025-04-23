package com.qtj4.ums.strategy;

import com.qtj4.ums.model.Course;

import java.util.Comparator;
import java.util.List;

public class SortByStartDate implements CourseSortingStrategy {
    @Override
    public void sort(List<Course> courses) {
        courses.sort(Comparator.comparing(Course::getStartDate));
    }
}