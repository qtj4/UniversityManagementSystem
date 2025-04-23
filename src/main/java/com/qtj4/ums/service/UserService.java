package com.qtj4.ums.service;

import com.qtj4.ums.dto.UserDTO;
import com.qtj4.ums.model.Role;
import com.qtj4.ums.model.User;
import com.qtj4.ums.repository.UserRepository;
import com.qtj4.ums.exception.NotFoundException;
import com.qtj4.ums.exception.ValidationException;
import com.qtj4.ums.factory.UserDTOFactory;
import com.qtj4.ums.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDTOFactory userDTOFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getUsersByRole(Role role) {
        logger.debug("Fetching users with role: {}", role);
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .map(userDTOFactory::create)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        logger.debug("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        return userDTOFactory.create(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        logger.debug("Creating user: {}", userDTO.getUsername());
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ValidationException("Username already exists: " + userDTO.getUsername());
        }
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode("defaultPassword"));
        user = userRepository.save(user);
        return userDTOFactory.create(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        logger.debug("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user = userRepository.save(user);
        return userDTOFactory.create(user);
    }

    public void deleteUser(Long id) {
        logger.debug("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}