package com.haykz.repository;

import com.haykz.entity.GarmentEntity;
import com.haykz.entity.Size;
import com.haykz.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for accessing and manipulating garment entities in the database.
 * <p>
 * This repository provides methods for querying garment data with specific criteria such as
 * garment type, size, and price range.
 * </p>
 */
@Repository
public interface GarmentRepository extends JpaRepository<GarmentEntity, Long> {

    /**
     * Finds a list of {@link GarmentEntity} based on the provided filtering criteria.
     * This method allows querying by garment type, size, and a price range.
     * <p>
     * If any parameter is {@code null}, it is ignored in the query. For example, if
     * {@code type} is {@code null}, it will not be used to filter the results.
     * </p>
     *
     * @param type The type of the garment (e.g., SHIRT, PANTS, etc.) to filter by, or {@code null} to ignore this filter
     * @param size The size of the garment (e.g., SMALL, MEDIUM, etc.) to filter by, or {@code null} to ignore this filter
     * @param minPrice The minimum price of the garment to filter by, or {@code null} to ignore this filter
     * @param maxPrice The maximum price of the garment to filter by, or {@code null} to ignore this filter
     * @return a list of garments that match the given criteria
     */
    @Query("SELECT g FROM GarmentEntity g WHERE " +
            "(:type IS NULL OR g.type = :type) AND " +
            "(:size IS NULL OR g.size = :size) AND " +
            "(:minPrice IS NULL OR g.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR g.price <= :maxPrice)")
    List<GarmentEntity> findByCriteria(
            @Param("type") Type type,
            @Param("size") Size size,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);
}
