package com.haykz.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) for creating a new user.
 *
 * This class represents the required details for creating a new user in the system.
 * It includes attributes such as the user's full name, address, username, and password.
 * The DTO is used to transfer user data from the client to the server when registering a new user.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDto {
    private String fullName;
    private String address;
    private String username;
    private String password;
}
