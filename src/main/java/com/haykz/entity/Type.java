package com.haykz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the types of garments.
 * Each type has a name, description, and season in which it is typically worn.
 */
@Getter
@AllArgsConstructor
public enum Type {
    SHIRT("Shirt", "Casual", "All Season"),
    PANTS("Pants", "Formal", "All Season"),
    JACKET("Jacket", "Outerwear", "Winter"),
    DRESS("Dress", "Formal", "Summer");

    private final String name;
    private final String description;
    private final String season;
}