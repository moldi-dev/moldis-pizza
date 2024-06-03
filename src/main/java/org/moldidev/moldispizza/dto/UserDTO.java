package org.moldidev.moldispizza.dto;

public record UserDTO(Long userId,
                      String username,
                      ImageDTO image,
                      String firstName,
                      String lastName,
                      String email,
                      String address) {
}
