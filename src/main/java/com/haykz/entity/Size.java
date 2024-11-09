package com.haykz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the sizes of garments.
 * Each size has a label, measurement range, and a region where it is commonly used.
 */
@Getter
@AllArgsConstructor
public enum Size {
    SMALL("S", "34-36", "US"),
    MEDIUM("M", "38-40", "US"),
    LARGE("L", "42-44", "US");

    private final String label;
    private final String measurement;
    private final String region;
}
