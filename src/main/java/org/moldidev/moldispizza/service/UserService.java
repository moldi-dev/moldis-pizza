package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.request.admin.UserCreateAdminRequest;
import org.moldidev.moldispizza.request.admin.UserDetailsUpdateAdminRequest;
import org.moldidev.moldispizza.request.customer.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public interface UserService {
    UserDTO save(UserCreateAdminRequest request);
    UserDTO save(UserSignUpRequest request);

    Map<String, String> signIn(UserSignInRequest request);
    User authenticate(String username, String password);

    UserDTO findById(Long userId);
    UserDTO findByUsername(String username, Authentication connectedUser);
    UserDTO findByEmail(String email);
    UserDTO findByVerificationToken(String verificationToken);
    Page<UserDTO> findAll(int page, int size);

    UserDTO verifyByVerificationToken(UserActivateAccountRequest request);
    void resendConfirmationEmail(String email);
    void sendResetPasswordEmail(UserResetPasswordEmailRequest request);
    UserDTO resetPasswordThroughToken(UserResetPasswordCodeRequest request);
    UserDTO changePasswordById(Long userId, UserChangePasswordRequest request, Authentication connectedUser);

    UserDTO setUserImage(Long userId, Long imageId, Authentication connectedUser);
    UserDTO removeUserImage(Long userId, Authentication connectedUser);

    UserDTO updateById(Long userId, UserDetailsUpdateRequest request, Authentication connectedUser);
    UserDTO updateById(Long userId, UserDetailsUpdateAdminRequest request);

    void deleteById(Long userId);

    Boolean checkIfUserIsAdmin(Long userId);
}
