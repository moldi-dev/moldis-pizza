package org.moldidev.moldispizza.audit;

import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.oauth2.CustomOAuth2User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOAuth2User customOAuth2User) {
            return Optional.of(customOAuth2User.getUser().getUsername());
        }

        else if (principal instanceof User user) {
            return Optional.of(user.getUsername());
        }

        return Optional.empty();
    }
}
