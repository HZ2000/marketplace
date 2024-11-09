package com.haykz.dto.authentication;

import lombok.*;

/**
 * Data Transfer Object (DTO) for authentication request.
 *
 * This class represents the data sent by the client to request authentication.
 * It contains the username and password of the user attempting to log in.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDto {
    private String username;
    private String password;
}