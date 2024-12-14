package org.ahroo.nexus.service;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.ahroo.nexus.dto.CategoryDTO;
import org.ahroo.nexus.dto.user.Activation;
import org.ahroo.nexus.dto.user.Principal;
import org.ahroo.nexus.dto.user.UserDTO;
import org.ahroo.nexus.entity.Category;
import org.ahroo.nexus.entity.Country;
import org.ahroo.nexus.entity.Token;
import org.ahroo.nexus.entity.User;
import org.ahroo.nexus.exception.token.TokenExpiredException;
import org.ahroo.nexus.exception.user.UserAlreadyVerifiedException;
import org.ahroo.nexus.exception.user.UserNotFoundException;
import org.ahroo.nexus.repository.UserRepository;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final CountryService countryService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationService authenticationService;

    private final CategoryService categoryService;

    private final TokenService tokenService;

    private final EmailService emailService;

    @Transactional
    public User createUser(UserDTO userDTO) {

        // 1. validate country
        Country country = countryService.findCountryByName(userDTO.getCountry());
        if (null == country) {
            throw new IllegalArgumentException("Invalid country: " + userDTO.getCountry());
        }

        // 2. check if user available in DB
        boolean exists = userRepository.existsUserByEmail(userDTO.getEmail());
        if (exists) {
            throw new EntityExistsException(String.format("User with email: %s already exists", userDTO.getEmail()));
        }

        // 3. encode the password
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());

        // 4. save the user
        User user = userDTO.toUser();
        user.setCountry(country);
        user.setPassword(encryptedPassword);

        user = userRepository.save(user);

        // 4. issue the token
        issueNewToken(user);

        return user;
    }

    /**
     * If user-id is same as authenticated user, show full profile.
     * Otherwise, show public profile.
     */
    public UserDTO findById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        try {
            authenticationService.isAuthorized(userId);

            // show private profile
            return UserDTO.fromUser(user);

        } catch (AuthenticationException authenticationException) {
            // show public profile
            return UserDTO.fromUserPublic(user);
        }
    }

    /**
     * The principal is a user with partial set of data from DB.
     * This method returns actual user from DB.
     */
    public User getActual(Principal principal) {
        return userRepository.findById(principal.getId()).orElseThrow(() -> new UserNotFoundException(principal.getId()));
    }

    @Transactional
    public Category subscribe(Integer userId, CategoryDTO categoryDTO) {
        // 1. verify user
        authenticationService.isAuthorized(userId);

        // 2. create categories
        Category category = categoryDTO.toCategory();

        // 3. save categories
        category = categoryService.save(category);

        // 4. add categories to user
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        var categories = user.getCategories();
        if (!categories.contains(category)) {
            categories.add(category);
            userRepository.save(user);
        }

        return category;
    }

    @Transactional
    public Category unsubscribe(Integer userId, CategoryDTO categoryDTO) {
        // 1. verify user
        authenticationService.isAuthorized(userId);

        // 2. create categories
        Category category = categoryDTO.toCategory();

        // 4. remove categories from user
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        var categories = user.getCategories();
        if (categories.contains(category)) {
            categories.remove(category);
            userRepository.save(user);
        }

        return category;
    }

    @Transactional
    public Activation activateUser(UUID tokenId) {

        Token token = tokenService.findByTokenId(tokenId);
        User claims = token.getUser();

        // 1. should not be deleted
        if (null == claims.getIsDeleted() || claims.getIsDeleted()) {
            throw new UserNotFoundException(claims.getEmail());
        }

        // 2. user should not be enabled
        if (null != claims.getIsEnabled() && claims.getIsEnabled()) {
            throw new UserAlreadyVerifiedException(claims.getEmail());
        }

        boolean tokenConfirmed = false;
        boolean isUserActivated = false;
        Boolean isNewTokenIssued = null;
        try {
            tokenConfirmed = tokenService.confirmToken(token);

            isUserActivated = userRepository.activateUser(claims.getEmail()) == 1;
        } catch (TokenExpiredException tokenExpiredException) {
            // generate new token and send to user
            issueNewToken(token.getUser());
            isNewTokenIssued = Boolean.TRUE;
        }

        return Activation.builder()
                .email(token.getUser().getEmail())
                .isActivated(tokenConfirmed && isUserActivated)
                .newTokenIssued(isNewTokenIssued)
                .build();
    }

    public Token issueNewToken(User user) {
        // 1. issue the token
        Token token = new Token();
        token.setTokenId(UUID.randomUUID());
        token.setCreatedOn(LocalDateTime.now());
        token.setExpiresOn(LocalDateTime.now().plusMinutes(10));
        token.setUser(user);

        token = tokenService.save(token);

        // 2. send email
        emailService.sendTokenAsEmail(user.getEmail(), token.getTokenId());

        return token;
    }
}
