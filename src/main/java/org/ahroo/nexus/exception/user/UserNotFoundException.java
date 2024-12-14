package org.ahroo.nexus.exception.user;

import lombok.Getter;
import org.ahroo.nexus.entity.User;
import org.ahroo.nexus.exception.common.IdNotFoundException;

@Getter
public class UserNotFoundException extends IdNotFoundException {

    public UserNotFoundException(final int id) {
        super(String.valueOf(id), User.class);
    }

    public UserNotFoundException(final String email) {
        super(email, User.class, String.format("%s with email: %s is not found", User.class, email));
    }
}
