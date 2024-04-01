package org.moldidev.moldispizza.dto;

public record UserDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        String address,
        String role,
        String email

) {
}
