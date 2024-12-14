package org.ahroo.nexus.service;

import lombok.RequiredArgsConstructor;
import org.ahroo.nexus.dto.user.Principal;
import org.ahroo.nexus.exception.user.UserNotFoundException;
import org.ahroo.nexus.exception.common.IllegalRequestException;
import org.ahroo.nexus.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Instead of getting authenticated user only in controllers, this facade allows to fetch from everywhere
 * <a href="https://www.baeldung.com/get-user-in-spring-security">source</a>
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * This is request scoped. User will not be visible for threads other than requesting thread.
     */
    public Principal getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isRealUser = !(authentication instanceof AnonymousAuthenticationToken);
        if (!isRealUser) {
            throw new AuthenticationServiceException("User not authenticated");
        }

        var principal = authentication.getPrincipal();

        // the wannabe user
        return (Principal) principal;
    }

    /**
     * A user has modification access to only his resources
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean isAuthorized(Integer userId) {
        var claims = getPrincipal();

        // 1. check if user enabled
        if (!claims.isEnabled() || !claims.isAccountNonExpired()) {
            throw new AuthenticationServiceException("User not authorized");
        }

        // 2. check if request is for the claimed user
        if (!claims.getId().equals(userId)) {
            throw new IllegalRequestException(claims);
        }

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return new Principal(user);
    }

}
