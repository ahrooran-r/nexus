package org.ahroo.nexus.dto.user;

import lombok.experimental.Delegate;
import org.ahroo.nexus.entity.User;
import org.ahroo.nexus.exception.user.UserNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public record Principal(@Delegate User user) implements UserDetails {

    public Principal {

        boolean preCondition = user == null || user.getId() == null || user.getEmail() == null || user.getPassword() == null;
        if (preCondition) throw new IllegalArgumentException("Invalid principal. Null implementation");

        if (null == user.getIsDeleted() || user.getIsDeleted()) {
            throw new UserNotFoundException(user.getEmail());
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // No user roles.
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * We are not doing email verification and other stuff.
     *
     * @return true if user is not deleted
     */
    @Override
    public boolean isEnabled() {
        return null != user.getIsEnabled() && user.getIsEnabled();
    }

    @Override
    public boolean isAccountNonExpired() {
        return null != user.getIsDeleted() && !user.getIsDeleted();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonExpired();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isAccountNonExpired();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Principal that = (Principal) obj;
        return this.user().equals(that.user());
    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }
}
