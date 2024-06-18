package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public interface UserService {
    UserDTO save(User user);

    Map<String, String> signIn(Map<String, Object> credentials);
    User authenticate(String username, String password);

    UserDTO findById(Long userId);
    UserDTO findByUsername(String username, Authentication connectedUser);
    UserDTO findByEmail(String email);
    UserDTO findByVerificationToken(String verificationToken);
    Page<UserDTO> findAll(int page, int size);

    ResponseEntity<String> verifyByVerificationToken(String email, String verificationToken);

    UserDTO setUserImage(Long userId, Long imageId, Authentication connectedUser);
    UserDTO removeUserImage(Long userId, Authentication connectedUser);

    UserDTO updateById(Long userId, User updatedUser, Authentication connectedUser);

    void deleteById(Long userId);
}
