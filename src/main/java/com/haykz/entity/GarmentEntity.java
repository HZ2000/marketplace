package com.haykz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity class representing a Garment.
 *
 * This class is mapped to the 'garment' table in the database and contains information about a garment,
 * including its type, description, size, price, and the publisher who created it.
 * The publisher is a relationship to the UserEntity.
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GarmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String description;

    @Enumerated(EnumType.STRING)
    private Size size;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private UserEntity publisher;
}
