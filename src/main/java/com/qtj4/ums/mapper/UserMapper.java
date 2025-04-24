package com.qtj4.ums.mapper;

import com.qtj4.ums.dto.UserDTO;
import com.qtj4.ums.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);
}