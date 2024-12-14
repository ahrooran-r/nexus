package org.ahroo.nexus.exception.token;

import lombok.Getter;
import org.ahroo.nexus.entity.Token;
import org.ahroo.nexus.exception.common.IdNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class TokenAlreadyConfirmedException extends IdNotFoundException {

    private final LocalDateTime confirmedOn;

    public TokenAlreadyConfirmedException(final UUID uuid, LocalDateTime confirmedOn) {
        super(uuid.toString(), Token.class, String.format("Token %s already confirmed", uuid));
        this.confirmedOn = confirmedOn;
    }
}
