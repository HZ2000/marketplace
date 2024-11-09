package com.haykz.service;

import com.haykz.dto.CreateUserDto;
import com.haykz.dto.UserDto;
import com.haykz.dto.authentication.AuthRequestDto;
import com.haykz.dto.authentication.AuthResponseDto;
import com.haykz.entity.UserEntity;
import com.haykz.repository.UserRepository;
import com.haykz.security.JWTUtil;
import com.haykz.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void authenticateUser_ShouldReturnAuthResponseDto_WhenCredentialsAreValid() {
        AuthRequestDto authRequest = AuthRequestDto.builder()
                .username("testuser")
                .password("password")
                .build();
        UserEntity user = new UserEntity();
        user.setUsername("testuser");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));
        when(jwtUtil.generateToken("testuser")).thenReturn("mocked-jwt-token");

        AuthResponseDto response = authService.authenticateUser(authRequest);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
    }

    @Test
    public void authenticateUser_ShouldThrowBadCredentialsException_WhenCredentialsAreInvalid() {
        AuthRequestDto authRequest = AuthRequestDto.builder()
                .username("testuser")
                .password("password")
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Incorrect username or password"));

        assertThrows(BadCredentialsException.class, () -> authService.authenticateUser(authRequest));
    }

    @Test
    public void findUserByUsername_ShouldReturnUserEntity_WhenUserExists() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));

        UserEntity foundUser = authService.findUserByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    public void findUserByUsername_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.findUserByUsername("nonexistentuser"));
    }

    @Test
    public void registerUser_ShouldReturnUserDto_WhenUsernameIsUnique() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .username("testuser")
                .password("password")
                .build();
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setPassword("encodedPassword");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(modelMapper.map(createUserDto, UserEntity.class)).thenReturn(userEntity);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(UserDto.builder().username("testuser").build());

        UserDto registeredUser = authService.registerUser(createUserDto);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
    }

    @Test
    public void registerUser_ShouldThrowIllegalArgumentException_WhenUsernameIsAlreadyTaken() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .username("existinguser")
                .password("password")
                .build();
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(createUserDto));
    }

}