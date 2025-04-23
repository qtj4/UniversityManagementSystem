package com.qtj4.ums.mapper;

import com.qtj4.ums.dto.UserDTO;
import com.qtj4.ums.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);
}