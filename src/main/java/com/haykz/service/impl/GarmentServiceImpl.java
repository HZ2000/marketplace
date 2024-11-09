package com.haykz.service.impl;

import com.haykz.dto.CreateGarmentDto;
import com.haykz.dto.GarmentDto;
import com.haykz.entity.GarmentEntity;
import com.haykz.entity.Size;
import com.haykz.entity.Type;
import com.haykz.entity.UserEntity;
import com.haykz.exception.ResourceNotFoundException;
import com.haykz.repository.GarmentRepository;
import com.haykz.repository.UserRepository;
import com.haykz.service.GarmentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link GarmentService} interface for managing garments.
 * <p>
 * This service provides methods for retrieving, publishing, updating, and unpublishing garments.
 * It interacts with the {@link GarmentRepository} and {@link UserRepository} to fetch and manipulate garment data
 * and uses {@link ModelMapper} to convert between entities and DTOs.
 * </p>
 */
@Service
@AllArgsConstructor
public class GarmentServiceImpl implements GarmentService {

    private final GarmentRepository garmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GarmentDto> getAllGarments(Type type, Size size, BigDecimal minPrice, BigDecimal maxPrice) {
        List<GarmentEntity> garments = garmentRepository.findByCriteria(type, size, minPrice, maxPrice);
        return garments.stream()
                .map(garment -> modelMapper.map(garment, GarmentDto.class))  // Map each Garment to GarmentDTO
                .collect(Collectors.toList());
    }

    @Override
    public GarmentDto getGarmentById(Long id) {
        GarmentEntity garment = garmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garment not found with id: " + id));
        return modelMapper.map(garment, GarmentDto.class);  // Map Garment to GarmentDTO
    }

    @Override
    public GarmentDto publishGarment(CreateGarmentDto createGarmentDTO, UserDetails userDetails) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        GarmentEntity garment = modelMapper.map(createGarmentDTO, GarmentEntity.class);
        garment.setPublisher(user);
        GarmentEntity savedGarment = garmentRepository.save(garment);
        return modelMapper.map(savedGarment, GarmentDto.class);  // Map saved Garment to GarmentDTO
    }

    @Override
    public GarmentDto updateGarment(Long id, CreateGarmentDto garmentDto, UserDetails userDetails) {
        GarmentEntity existingGarment = garmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garment not found with id: " + id));

        if (!existingGarment.getPublisher().getUsername().equals(userDetails.getUsername())) {
            throw new IllegalStateException("You are not authorized to update this garment");
        }

        modelMapper.map(garmentDto, existingGarment);
        GarmentEntity updatedGarment = garmentRepository.save(existingGarment);
        return modelMapper.map(updatedGarment, GarmentDto.class);
    }

    @Override
    public void unpublishGarment(Long id, UserDetails userDetails) {
        GarmentEntity existingGarment = garmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garment not found with id: " + id));

        // Check if the user is the publisher of the garment
        if (!existingGarment.getPublisher().getUsername().equals(userDetails.getUsername())) {
            throw new IllegalStateException("You are not authorized to unpublish this garment");
        }

        garmentRepository.delete(existingGarment);
    }
}
