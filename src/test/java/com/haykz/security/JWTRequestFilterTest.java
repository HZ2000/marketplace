package com.haykz.security;

import com.haykz.service.impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JWTRequestFilterTest {

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private RequestMatcher ignoredPaths; // Mock ignoredPaths

    private JWTRequestFilter jwtRequestFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();

        jwtRequestFilter = new JWTRequestFilter(ignoredPaths, jwtUtil, userDetailsService);
    }

    @Test
    public void doFilterInternal_ShouldAuthenticateUser_WhenJwtIsValid() throws ServletException, IOException {
        String jwt = "valid-jwt";
        String username = "testuser";
        request.setRequestURI("/some/other/path");
        request.addHeader("Authorization", "Bearer " + jwt);

        UserDetails userDetails = new User(username, "password", new ArrayList<>());

        when(this.ignoredPaths.matches(request)).thenReturn(true);
        when(jwtUtil.extractUsername(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(jwt, username)).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication, "Authentication should not be null");
        assertEquals(username, authentication.getName());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_ShouldSkipFilter_WhenPathIsIgnored() throws ServletException, IOException {
        request.setRequestURI("/user/clothes/123");

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }


    @Test
    public void doFilterInternal_ShouldReturnUnauthorized_WhenJwtIsInvalid() throws ServletException, IOException {
        String jwt = "invalid-jwt";
        request.setRequestURI("/some/other/path");
        request.addHeader("Authorization", "Bearer " + jwt);
        UserDetails userDetails = new User("username", "password", new ArrayList<>()); // Mocked UserDetails instance

        when(this.ignoredPaths.matches(request)).thenReturn(true);
        when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails); // Ensure this returns a UserDetails
        when(jwtUtil.extractUsername(jwt)).thenReturn("testuser");
        when(jwtUtil.validateToken(jwt, "testuser")).thenReturn(false);  // Simulate invalid token

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verify(filterChain, never()).doFilter(request, response);
    }


    @Test
    public void doFilterInternal_ShouldReturnUnauthorized_WhenAuthorizationHeaderIsMissing() throws ServletException, IOException {
        request.setRequestURI("/some/other/path");
        when(this.ignoredPaths.matches(request)).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_ShouldReturnUnauthorized_WhenUserNotFound() throws ServletException, IOException {
        String jwt = "valid-jwt";
        String username = "unknownuser";
        request.setRequestURI("/some/other/path");
        request.addHeader("Authorization", "Bearer " + jwt);

        when(this.ignoredPaths.matches(request)).thenReturn(true);
        when(jwtUtil.extractUsername(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new RuntimeException("User not found"));

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Authorization error: User not found", response.getContentAsString());
        verify(filterChain, never()).doFilter(request, response);
    }
}
