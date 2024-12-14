package org.ahroo.nexus.dto.user;

import lombok.Builder;

@Builder
public record Activation(
        String email,
        Boolean isActivated,
        Boolean newTokenIssued
) {
}
