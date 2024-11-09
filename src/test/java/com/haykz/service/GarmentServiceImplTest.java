package com.haykz.service;

import com.haykz.dto.CreateGarmentDto;
import com.haykz.dto.GarmentDto;
import com.haykz.entity.GarmentEntity;
import com.haykz.entity.Type;
import com.haykz.entity.UserEntity;
import com.haykz.exception.ResourceNotFoundException;
import com.haykz.repository.GarmentRepository;
import com.haykz.repository.UserRepository;
import com.haykz.service.impl.GarmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GarmentServiceImplTest {

    @Mock
    private GarmentRepository garmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private ModelMapper modelMapper;

    @InjectMocks
    private GarmentServiceImpl garmentService;

    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = new User("testuser", "password", new ArrayList<>());
    }

    @Test
    public void getAllGarments_ShouldReturnListOfGarmentDtos_WhenGarmentsExist() {
        List<GarmentEntity> garmentEntities = List.of(new GarmentEntity(), new GarmentEntity());
        when(garmentRepository.findByCriteria(null, null, null, null)).thenReturn(garmentEntities);
        when(modelMapper.map(any(GarmentEntity.class), eq(GarmentDto.class)))
                .thenReturn(new GarmentDto());

        List<GarmentDto> garmentDtos = garmentService.getAllGarments(null, null, null, null);

        assertEquals(2, garmentDtos.size());
    }

    @Test
    public void getGarmentById_ShouldReturnGarmentDto_WhenGarmentExists() {
        GarmentEntity garmentEntity = new GarmentEntity();
        when(garmentRepository.findById(1L)).thenReturn(Optional.of(garmentEntity));
        when(modelMapper.map(garmentEntity, GarmentDto.class)).thenReturn(new GarmentDto());

        GarmentDto garmentDto = garmentService.getGarmentById(1L);

        assertNotNull(garmentDto);
    }

    @Test
    public void getGarmentById_ShouldThrowResourceNotFoundException_WhenGarmentDoesNotExist() {
        when(garmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> garmentService.getGarmentById(1L));
    }

    @Test
    public void publishGarment_ShouldReturnGarmentDto_WhenUserExists() {
        CreateGarmentDto createGarmentDto = new CreateGarmentDto();
        UserEntity userEntity = new UserEntity();
        GarmentEntity garmentEntity = new GarmentEntity();
        GarmentDto garmentDto = new GarmentDto();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(createGarmentDto, GarmentEntity.class)).thenReturn(garmentEntity);
        when(garmentRepository.save(garmentEntity)).thenReturn(garmentEntity);
        when(modelMapper.map(garmentEntity, GarmentDto.class)).thenReturn(garmentDto);

        GarmentDto result = garmentService.publishGarment(createGarmentDto, userDetails);

        assertNotNull(result);
    }

    @Test
    public void publishGarment_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        CreateGarmentDto createGarmentDto = new CreateGarmentDto();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> garmentService.publishGarment(createGarmentDto, userDetails));
    }

    @Test
    public void updateGarment_ShouldReturnUpdatedGarmentDto_WhenAuthorized() {
        CreateGarmentDto createGarmentDto = CreateGarmentDto.builder()
                .type(Type.SHIRT)
                .price(BigDecimal.TEN)
                .build();
        GarmentEntity existingGarment = new GarmentEntity();
        existingGarment.setId(1L);
        existingGarment.setPublisher(new UserEntity(1L, "Test", "Test Add", "testuser", "password"));
        GarmentDto updatedGarmentDto = new GarmentDto();

        when(garmentRepository.findById(1L)).thenReturn(Optional.of(existingGarment));
        when(garmentRepository.save(existingGarment)).thenReturn(existingGarment);
        when(modelMapper.map(existingGarment, GarmentDto.class)).thenReturn(updatedGarmentDto);

        GarmentDto result = garmentService.updateGarment(1L, createGarmentDto, userDetails);

        assertNotNull(result);
    }

    @Test
    public void updateGarment_ShouldThrowResourceNotFoundException_WhenGarmentDoesNotExist() {
        CreateGarmentDto createGarmentDto = new CreateGarmentDto();
        when(garmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> garmentService.updateGarment(1L, createGarmentDto, userDetails));
    }

    @Test
    public void updateGarment_ShouldThrowIllegalStateException_WhenUserIsNotAuthorized() {
        CreateGarmentDto createGarmentDto = new CreateGarmentDto();
        GarmentEntity existingGarment = new GarmentEntity();
        existingGarment.setPublisher(new UserEntity(1L, "Test", "Test Add", "testuser1", "password"));

        when(garmentRepository.findById(1L)).thenReturn(Optional.of(existingGarment));

        assertThrows(IllegalStateException.class, () -> garmentService.updateGarment(1L, createGarmentDto, userDetails));
    }

    @Test
    public void unpublishGarment_ShouldDeleteGarment_WhenUserIsAuthorized() {
        GarmentEntity existingGarment = new GarmentEntity();
        existingGarment.setPublisher(new UserEntity(1L, "Test", "Test Add", "testuser", "password"));

        when(garmentRepository.findById(1L)).thenReturn(Optional.of(existingGarment));

        garmentService.unpublishGarment(1L, userDetails);

        verify(garmentRepository, times(1)).delete(existingGarment);
    }

    @Test
    public void unpublishGarment_ShouldThrowResourceNotFoundException_WhenGarmentDoesNotExist() {
        when(garmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> garmentService.unpublishGarment(1L, userDetails));
    }

    @Test
    public void unpublishGarment_ShouldThrowIllegalStateException_WhenUserIsNotAuthorized() {
        GarmentEntity existingGarment = new GarmentEntity();
        existingGarment.setPublisher(new UserEntity(1L, "Test", "Test Add", "testuser1", "password"));

        when(garmentRepository.findById(1L)).thenReturn(Optional.of(existingGarment));

        assertThrows(IllegalStateException.class, () -> garmentService.unpublishGarment(1L, userDetails));
    }
}

