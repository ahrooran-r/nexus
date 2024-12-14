package org.ahroo.nexus.repository;

import org.ahroo.nexus.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("select new Token(t.confirmedOn, t.expiresOn, tu.id, tu.email, tu.isEnabled, tu.isDeleted) from Token t join t.user tu where t.tokenId=:tokenId")
    Optional<Token> findByTokenId(UUID tokenId);

    @Query("update Token t set t.confirmedOn=CURRENT_TIMESTAMP where t.tokenId=:tokenId")
    @Modifying
    Integer confirmToken(UUID tokenId);
}
