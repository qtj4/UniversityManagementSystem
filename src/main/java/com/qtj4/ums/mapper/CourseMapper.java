package com.qtj4.ums.mapper;

import com.qtj4.ums.dto.CourseDTO;
import com.qtj4.ums.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(source = "teacher.id", target = "teacherId")
    CourseDTO toDto(Course course);
    @Mapping(target = "teacher", ignore = true)
    Course toEntity(CourseDTO courseDTO);
}