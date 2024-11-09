package com.haykz.dto;

import com.haykz.entity.Size;
import com.haykz.entity.Type;
import lombok.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing a garment.
 *
 * This class holds the details of a garment, including its unique identifier, type,
 * description, size, price, and the user who published the garment. It is used for
 * transferring garment information between different layers of the application.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarmentDto {
    private Long id;
    private Type type;
    private String description;
    private Size size;
    private BigDecimal price;
    private UserDto publisher;
}
