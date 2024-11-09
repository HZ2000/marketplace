package com.haykz.controller;

import com.haykz.dto.CreateGarmentDto;
import com.haykz.dto.GarmentDto;
import com.haykz.entity.Size;
import com.haykz.entity.Type;
import com.haykz.exception.ResourceNotFoundException;
import com.haykz.service.GarmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GarmentUserControllerTest {

    @Mock
    private GarmentService garmentService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private GarmentUserController garmentUserController;

    @Test
    public void publishGarment_ShouldReturnGarmentDto_WhenValidRequest() {
        CreateGarmentDto createGarmentDto = CreateGarmentDto.builder()
                .type(Type.SHIRT)
                .size(Size.MEDIUM)
                .description("Clothing")
                .price(BigDecimal.TWO)
                .build();
        GarmentDto garmentDto = GarmentDto.builder()
                .id(1L)
                .type(Type.SHIRT)
                .size(Size.MEDIUM)
                .description("Clothing")
                .price(BigDecimal.TWO)
                .build();

        when(garmentService.publishGarment(any(CreateGarmentDto.class), any(UserDetails.class))).thenReturn(garmentDto);

        ResponseEntity<?> response = garmentUserController.publishGarment(createGarmentDto, userDetails);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Type.SHIRT, ((GarmentDto) response.getBody()).getType());
    }

    @Test
    public void publishGarment_ShouldReturnBadRequest_WhenResourceNotFound() {
        CreateGarmentDto createGarmentDto = createGarmentDto();

        when(garmentService.publishGarment(any(CreateGarmentDto.class), any(UserDetails.class)))
                .thenThrow(new ResourceNotFoundException("Garment not found"));

        ResponseEntity<?> response = garmentUserController.publishGarment(createGarmentDto, userDetails);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Garment not found", response.getBody());  // Expecting exception message in response
    }

    @Test
    public void updateGarment_ShouldReturnUpdatedGarment_WhenValidRequest() {
        Long garmentId = 1L;
        CreateGarmentDto createGarmentDto = createGarmentDto();
        GarmentDto updatedGarmentDto = GarmentDto.builder()
                .id(garmentId)
                .type(Type.SHIRT)
                .size(Size.LARGE)
                .description("Clothing")
                .price(BigDecimal.TWO)
                .build();

        when(garmentService.updateGarment(any(Long.class), any(CreateGarmentDto.class), any(UserDetails.class)))
                .thenReturn(updatedGarmentDto);

        ResponseEntity<?> response = garmentUserController.updateGarment(garmentId, createGarmentDto, userDetails);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Type.SHIRT, ((GarmentDto) response.getBody()).getType());
        assertEquals(Size.LARGE, ((GarmentDto) response.getBody()).getSize());
    }

    @Test
    public void updateGarment_ShouldReturnBadRequest_WhenResourceNotFound() {
        Long garmentId = 999L;
        CreateGarmentDto createGarmentDto = createGarmentDto();

        when(garmentService.updateGarment(any(Long.class), any(CreateGarmentDto.class), any(UserDetails.class)))
                .thenThrow(new ResourceNotFoundException("Garment not found"));

        ResponseEntity<?> response = garmentUserController.updateGarment(garmentId, createGarmentDto, userDetails);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Garment not found", response.getBody());
    }

    private CreateGarmentDto createGarmentDto() {
        return CreateGarmentDto.builder()
                .type(Type.SHIRT)
                .size(Size.LARGE)
                .description("Clothing")
                .price(BigDecimal.TWO)
                .build();
    }
}

