package com.qtj4.ums.mapper;

import com.qtj4.ums.dto.CourseDTO;
import com.qtj4.ums.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "teacherId", source = "teacher.id")
    CourseDTO toDto(Course course);

    Course toEntity(CourseDTO courseDTO);
}