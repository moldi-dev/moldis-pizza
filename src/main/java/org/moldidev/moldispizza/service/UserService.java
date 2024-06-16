package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface UserService {
    UserDTO save(User user);

    User authenticate(String username, String password);

    UserDTO findById(Long userId);
    UserDTO findByUsername(String username);
    UserDTO findByEmail(String email);
    UserDTO findByVerificationToken(String verificationToken);
    Page<UserDTO> findAll(int page, int size);

    ResponseEntity<String> verifyByVerificationToken(String email, String verificationToken);

    UserDTO setUserImage(Long userId, Long imageId);
    UserDTO removeUserImage(Long userId);

    UserDTO updateById(Long userId, User updatedUser);

    void deleteById(Long userId);
}
