package com.haykz.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) representing a user.
 *
 * This class holds the details of a user, including their unique identifier, full name,
 * address, and username. It is used to transfer user information between different layers
 * of the application without exposing sensitive information like passwords.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String fullName;
    private String address;
    private String username;
}
