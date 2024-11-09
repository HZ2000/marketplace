package com.haykz.controller;

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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GarmentControllerTest {

    @Mock
    private GarmentService garmentService;

    @InjectMocks
    private GarmentController garmentController;

    @Test
    public void getAllClothes_ShouldReturnGarmentList_WhenValidParams() {
        GarmentDto garment1 = GarmentDto.builder()
                .id(1L)
                .type(Type.SHIRT)
                .size(Size.MEDIUM)
                .price(BigDecimal.TEN)
                .build();
        GarmentDto garment2 = GarmentDto.builder()
                .id(2L)
                .type(Type.PANTS)
                .size(Size.LARGE)
                .price(BigDecimal.TWO)
                .build();
        List<GarmentDto> garments = Arrays.asList(garment1, garment2);

        when(garmentService.getAllGarments(any(), any(), any(), any())).thenReturn(garments);

        ResponseEntity<List<GarmentDto>> response = garmentController.getAllClothes(null, null, null, null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void getAllClothes_ShouldReturnEmptyList_WhenNoGarmentsMatch() {
        when(garmentService.getAllGarments(any(), any(), any(), any())).thenReturn(List.of());

        ResponseEntity<List<GarmentDto>> response = garmentController.getAllClothes(null, null, null, null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void getClothes_ShouldReturnGarment_WhenGarmentExists() {
        GarmentDto garment = GarmentDto.builder()
                .id(1L)
                .type(Type.SHIRT)
                .size(Size.MEDIUM)
                .price(BigDecimal.TEN)
                .build();

        when(garmentService.getGarmentById(1L)).thenReturn(garment);

        ResponseEntity<?> response = garmentController.getClothes(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Type.SHIRT, ((GarmentDto) response.getBody()).getType());
    }

    @Test
    public void getClothes_ShouldReturnBadRequest_WhenGarmentNotFound() {
        when(garmentService.getGarmentById(999L)).thenThrow(new ResourceNotFoundException("Garment not found"));

        ResponseEntity<?> response = garmentController.getClothes(999L);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Garment not found", response.getBody());
    }
}

