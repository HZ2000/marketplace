package com.haykz.service;

import com.haykz.dto.CreateGarmentDto;
import com.haykz.dto.GarmentDto;
import com.haykz.entity.Size;
import com.haykz.entity.Type;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface for handling operations related to garments in the application.
 * <p>
 * This service provides methods for retrieving, publishing, updating, and deleting garments.
 * The methods allow for filtering garments based on criteria, managing garment details,
 * and associating garments with users.
 * </p>
 */
public interface GarmentService {
    List<GarmentDto> getAllGarments(Type type, Size size, BigDecimal minPrice, BigDecimal maxPrice);
    GarmentDto getGarmentById(Long id);
    GarmentDto publishGarment(CreateGarmentDto createGarmentDTO, UserDetails userDetails);
    GarmentDto updateGarment(Long id, CreateGarmentDto garmentDto, UserDetails userDetails);
    void unpublishGarment(Long id, UserDetails userDetails);
}