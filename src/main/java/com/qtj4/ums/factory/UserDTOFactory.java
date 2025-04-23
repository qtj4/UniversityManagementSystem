package com.qtj4.ums.factory;

import com.qtj4.ums.dto.UserDTO;
import com.qtj4.ums.model.User;
import com.qtj4.ums.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDTOFactory {
    @Autowired
    private UserMapper userMapper;

    public UserDTO create(User user) {
        return userMapper.toDto(user);
    }
}