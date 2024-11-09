package com.haykz.dto;

import com.haykz.entity.Size;
import com.haykz.entity.Type;
import lombok.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for creating a new garment.
 *
 * This class represents the required details for creating a garment in the system.
 * It includes attributes such as the garment's type, description, size, and price.
 * The DTO is used to transfer garment data from the client to the server when creating a new garment.
 */
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGarmentDto {
    private Type type;
    private String description;
    private Size size;
    private BigDecimal price;
}
