package com.haykz.controller;

import com.haykz.dto.GarmentDto;
import com.haykz.entity.Size;
import com.haykz.entity.Type;
import com.haykz.exception.ResourceNotFoundException;
import com.haykz.service.GarmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * The GarmentController class is responsible for handling requests related to garments.
 * It exposes the following endpoints:
 * 1. /clothes - for retrieving a list of all garments, optionally filtered by type, size, and price range.
 * 2. /clothes/{id} - for retrieving a single garment by its ID.
 *
 * The controller delegates the actual logic to the GarmentService.
 */
@RestController
@RequestMapping("/clothes")
@AllArgsConstructor
public class GarmentController {

    private final GarmentService garmentService;

    /**
     * Retrieves a list of garments, optionally filtered by type, size, and price range.
     *
     * @param type The type of the garment (optional)
     * @param size The size of the garment (optional)
     * @param minPrice The minimum price filter for the garments (optional)
     * @param maxPrice The maximum price filter for the garments (optional)
     * @return a ResponseEntity containing the list of garments matching the filters
     */
    @GetMapping
    public ResponseEntity<List<GarmentDto>> getAllClothes(
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) Size size,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        List<GarmentDto> garments = garmentService.getAllGarments(type, size, minPrice, maxPrice);
        return ResponseEntity.ok(garments);
    }

    /**
     * Retrieves a specific garment by its ID.
     *
     * @param id The ID of the garment to retrieve
     * @return a ResponseEntity containing the garment details if found, or an error message if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getClothes(@PathVariable Long id) {
        try {
            GarmentDto garment = garmentService.getGarmentById(id);
            return ResponseEntity.ok(garment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
