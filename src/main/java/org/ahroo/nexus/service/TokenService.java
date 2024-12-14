package org.ahroo.nexus.service;

import lombok.RequiredArgsConstructor;
import org.ahroo.nexus.entity.Token;
import org.ahroo.nexus.exception.token.TokenAlreadyConfirmedException;
import org.ahroo.nexus.exception.token.TokenExpiredException;
import org.ahroo.nexus.exception.token.TokenNotFoundException;
import org.ahroo.nexus.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token save(Token token) {
        return tokenRepository.save(token);
    }

    @Transactional(noRollbackFor = TokenExpiredException.class)
    public boolean confirmToken(Token token) {

        // 3. check token confirmed / not
        if (null != token.getConfirmedOn()) {
            throw new TokenAlreadyConfirmedException(token.getTokenId(), token.getConfirmedOn());
        }

        // 4. check expiry
        if (null == token.getExpiresOn() || token.getExpiresOn().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(token.getTokenId(), token.getExpiresOn());
        }

        return tokenRepository.confirmToken(token.getTokenId()) == 1;
    }

    public Token findByTokenId(UUID tokenId) {
        //noinspection UnnecessaryLocalVariable
        var token = tokenRepository.findByTokenId(tokenId).orElseThrow(() -> new TokenNotFoundException(tokenId));
        token.setTokenId(tokenId);
        return token;
    }

}
