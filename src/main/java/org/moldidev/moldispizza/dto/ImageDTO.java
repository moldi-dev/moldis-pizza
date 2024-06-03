package org.moldidev.moldispizza.dto;

public record ImageDTO(Long imageId,
                       String name,
                       String type,
                       byte[] data) {
}
