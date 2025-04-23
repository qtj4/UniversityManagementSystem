package com.qtj4.ums.mapper;

import com.qtj4.ums.dto.EnrollmentDTO;
import com.qtj4.ums.model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "course.id", target = "courseId")
    EnrollmentDTO toDto(Enrollment enrollment);
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    Enrollment toEntity(EnrollmentDTO enrollmentDTO);
}