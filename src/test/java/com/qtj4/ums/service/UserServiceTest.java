package com.qtj4.ums.service;

import com.qtj4.ums.dto.UserDTO;
import com.qtj4.ums.model.Role;
import com.qtj4.ums.model.User;
import com.qtj4.ums.repository.UserRepository;
import com.qtj4.ums.exception.NotFoundException;
import com.qtj4.ums.exception.ValidationException;
import com.qtj4.ums.factory.UserDTOFactory;
import com.qtj4.ums.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserDTOFactory userDTOFactory;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRole(Role.STUDENT);
        user.setPassword("encodedPassword");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setName("Test User");
        userDTO.setEmail("test@example.com");
        userDTO.setRole(Role.STUDENT);
    }

    @Test
    void getUsersByRole_success() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userDTOFactory.create(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.getUsersByRole(Role.STUDENT);

        assertEquals(1, result.size());
        assertEquals(userDTO, result.get(0));
        verify(userRepository).findAll();
        verify(userDTOFactory).create(user);
    }

    @Test
    void getUserById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userDTOFactory.create(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        assertEquals(userDTO, result);
        verify(userRepository).findById(1L);
        verify(userDTOFactory).create(user);
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    void createUser_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userDTOFactory.create(user)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertEquals(userDTO, result);
        verify(userRepository).findByUsername("testuser");
        verify(userMapper).toEntity(userDTO);
        verify(passwordEncoder).encode("defaultPassword");
        verify(userRepository).save(user);
        verify(userDTOFactory).create(user);
    }

    @Test
    void createUser_usernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class, () -> userService.createUser(userDTO));
        verify(userRepository).findByUsername("testuser");
    }
}