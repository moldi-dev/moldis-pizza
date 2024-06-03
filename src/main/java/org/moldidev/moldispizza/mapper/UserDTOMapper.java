package org.moldidev.moldispizza.mapper;

import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.Image;
import org.moldidev.moldispizza.entity.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    private final ImageDTOMapper imageDTOMapper;

    public UserDTOMapper(ImageDTOMapper imageDTOMapper) {
        this.imageDTOMapper = imageDTOMapper;
    }

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getImage() != null ? imageDTOMapper.apply(user.getImage()) : null,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress()
        );
    }
}
