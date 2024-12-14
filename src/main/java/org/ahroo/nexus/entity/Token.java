package org.ahroo.nexus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ahroo.nexus.entity.common.WritableBaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tokens", schema = "nexus")
@Getter
@Setter
@NoArgsConstructor
public class Token extends WritableBaseEntity {

    public Token(LocalDateTime confirmedOn, LocalDateTime expiresOn, Integer userId, String userEmail, Boolean isUserEnabled, Boolean isUserDeleted) {
        this.expiresOn = expiresOn;
        this.confirmedOn = confirmedOn;

        User user = new User();
        user.setId(userId);
        user.setIsEnabled(isUserEnabled);
        user.setIsDeleted(isUserDeleted);
        user.setEmail(userEmail);
        this.user = user;

    }

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "expires_on", nullable = false, updatable = false)
    private LocalDateTime expiresOn;

    @Column(name = "confirmed_on")
    private LocalDateTime confirmedOn;

    @Column(name = "token_id", nullable = false, updatable = false)
    private UUID tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
