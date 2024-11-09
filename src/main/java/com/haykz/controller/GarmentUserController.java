package com.haykz.controller;

import com.haykz.dto.CreateGarmentDto;
import com.haykz.dto.GarmentDto;
import com.haykz.exception.ResourceNotFoundException;
import com.haykz.service.GarmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * The GarmentUserController class handles user-specific operations related to garments.
 * It provides the following functionalities:
 * 1. Publish a new garment.
 * 2. Update an existing garment.
 * 3. Unpublish an existing garment.
 *
 * All actions require the user to be authenticated, and operations are performed based on the user's credentials.
 */
@RestController
@RequestMapping("/user/clothes")
public class GarmentUserController {

    private final GarmentService garmentService;

    /**
     * Constructor that initializes the GarmentService instance.
     *
     * @param garmentService the GarmentService used for garment-related operations
     */
    @Autowired
    public GarmentUserController(GarmentService garmentService) {
        this.garmentService = garmentService;
    }

    /**
     * Endpoint to publish a new garment.
     *
     * @param garmentEntity The details of the garment to be published
     * @param userDetails The authenticated user's details
     * @return ResponseEntity with the published garment or an error message
     */
    @PostMapping
    public ResponseEntity<?> publishGarment(@RequestBody CreateGarmentDto garmentEntity,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            GarmentDto publishedGarment = garmentService.publishGarment(garmentEntity, userDetails);
            return ResponseEntity.ok(publishedGarment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Endpoint to update an existing garment.
     *
     * @param id The ID of the garment to be updated
     * @param garmentEntityDetails The updated details of the garment
     * @param userDetails The authenticated user's details
     * @return ResponseEntity with the updated garment or an error message
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGarment(@PathVariable Long id,
                                           @RequestBody CreateGarmentDto garmentEntityDetails,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            GarmentDto updatedGarmentEntity = garmentService.updateGarment(id, garmentEntityDetails, userDetails);
            return ResponseEntity.ok(updatedGarmentEntity);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Endpoint to unpublish an existing garment.
     *
     * @param id The ID of the garment to be unpublished
     * @param userDetails The authenticated user's details
     * @return ResponseEntity indicating whether the operation was successful or failed
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> unpublishGarment(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            garmentService.unpublishGarment(id, userDetails);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
