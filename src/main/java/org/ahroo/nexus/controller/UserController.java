package org.ahroo.nexus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahroo.nexus.dto.user.Activation;
import org.ahroo.nexus.dto.CategoryDTO;
import org.ahroo.nexus.dto.user.UserDTO;
import org.ahroo.nexus.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> post(
            @RequestBody @Valid final UserDTO request
    ) {
        var user = userService.createUser(request);
        var userDTO = UserDTO.fromUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(userDTO.getId()).toUri();

        return ResponseEntity
                .created(location)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTO);
    }

    @PostMapping("/{user-id}/subscribe")
    public ResponseEntity<CategoryDTO> subscribeToCategory(
            @PathVariable("user-id") final Integer userId,
            @RequestBody final CategoryDTO request
    ) {
        var subscribed = userService.subscribe(userId, request);
        var categoryDTO = CategoryDTO.fromCategory(subscribed);
        return ResponseEntity.ok(categoryDTO);
    }

    @DeleteMapping("/{user-id}/unsubscribe")
    public ResponseEntity<CategoryDTO> unsubscribeToCategory(
            @PathVariable("user-id") final Integer userId,
            @RequestBody final CategoryDTO request
    ) {
        var unsubscribed = userService.unsubscribe(userId, request);
        var categoryDTO = CategoryDTO.fromCategory(unsubscribed);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserDTO> userProfile(
            @PathVariable("user-id") final Integer userId
    ) {
        var userDTO = userService.findById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/activate")
    public ResponseEntity<Activation> activation(
            @RequestParam("token-id") final UUID tokenId
    ) {
        var activationDTO = userService.activateUser(tokenId);
        if (activationDTO.isActivated()) {
            return ResponseEntity.ok(activationDTO);
        } else {
            return ResponseEntity.badRequest().body(activationDTO);
        }
    }
}
