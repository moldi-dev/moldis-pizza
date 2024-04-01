package org.moldidev.moldispizza.mapper;

import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getRole(),
                user.getEmail());
    }
}
