package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.enumeration.Role;
import org.moldidev.moldispizza.exception.OperationNotPermittedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public void validateAuthenticatedUser(Authentication connectedUser, Long resourceUserId) {
        User authenticatedUser = ((User) connectedUser.getPrincipal());

        if (!authenticatedUser.getUserId().equals(resourceUserId) && authenticatedUser.getRole() == Role.CUSTOMER) {
            throw new OperationNotPermittedException();
        }
    }
}
