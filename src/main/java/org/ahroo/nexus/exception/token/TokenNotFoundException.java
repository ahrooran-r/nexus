package org.ahroo.nexus.exception.token;

import lombok.Getter;
import org.ahroo.nexus.entity.Token;
import org.ahroo.nexus.exception.common.IdNotFoundException;

import java.util.UUID;

@Getter
public class TokenNotFoundException extends IdNotFoundException {

    public TokenNotFoundException(final UUID uuid) {
        super(uuid.toString(), Token.class);
    }
}
