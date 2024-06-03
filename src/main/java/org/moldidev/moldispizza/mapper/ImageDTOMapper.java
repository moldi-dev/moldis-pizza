package org.moldidev.moldispizza.mapper;

import org.moldidev.moldispizza.dto.ImageDTO;
import org.moldidev.moldispizza.entity.Image;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ImageDTOMapper implements Function<Image, ImageDTO> {
    @Override
    public ImageDTO apply(Image image) {
        return new ImageDTO(
                image.getImageId(),
                image.getName(),
                image.getType(),
                image.getData()
        );
    }
}
