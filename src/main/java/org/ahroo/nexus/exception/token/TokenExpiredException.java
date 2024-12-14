package org.ahroo.nexus.exception.token;

import lombok.Getter;
import org.ahroo.nexus.entity.Token;
import org.ahroo.nexus.exception.common.IdNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class TokenExpiredException extends IdNotFoundException {

    private final LocalDateTime expiredOn;

    public TokenExpiredException(final UUID uuid, LocalDateTime expiredOn) {
        super(uuid.toString(), Token.class, String.format("Token %s expired", uuid));
        this.expiredOn = expiredOn;
    }
}
