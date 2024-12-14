package org.ahroo.nexus.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExceptionResponse(
        LocalDateTime timestamp,
        String message,
        String metaData
) {
}
