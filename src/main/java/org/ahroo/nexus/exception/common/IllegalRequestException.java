package org.ahroo.nexus.exception.common;

import lombok.Getter;
import org.ahroo.nexus.dto.user.Principal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Getter
public class IllegalRequestException extends AccessDeniedException {

    private final URI resource;

    private final Principal intruder;

    public IllegalRequestException(Principal intruder) {
        super(String.format("User: %s is trying to access %s", intruder.getEmail(), ServletUriComponentsBuilder.fromCurrentRequestUri().build(false).toUri()));
        this.resource = ServletUriComponentsBuilder.fromCurrentRequest().build(false).toUri();
        this.intruder = intruder;
    }
}
