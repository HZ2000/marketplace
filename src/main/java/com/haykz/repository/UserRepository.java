package com.haykz.repository;

import com.haykz.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing and manipulating user entities in the database.
 * <p>
 * This repository provides methods for querying user data by their username,
 * as well as checking if a user with a given username already exists.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
}
