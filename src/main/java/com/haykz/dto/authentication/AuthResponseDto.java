package com.haykz.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) for authentication response.
 *
 * This class represents the response data sent by the server after a successful authentication request.
 * It contains the JWT token that will be used for subsequent authenticated requests.
 */
@Data
@Builder
@AllArgsConstructor
@Getter
public class AuthResponseDto {
    private String token;
}
