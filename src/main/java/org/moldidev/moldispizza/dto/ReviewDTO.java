package org.moldidev.moldispizza.dto;

public record ReviewDTO(Long reviewId,
                        UserDTO userDTO,
                        Integer rating,
                        String comment,
                        PizzaDTO pizza) {
}
