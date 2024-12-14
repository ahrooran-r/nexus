package org.ahroo.nexus.exception.user;

import org.ahroo.nexus.entity.User;
import org.ahroo.nexus.exception.common.IdNotFoundException;

public class UserAlreadyVerifiedException extends IdNotFoundException {

    public UserAlreadyVerifiedException(String email) {
        super(email, User.class, String.format("%s with email: %s already verified", User.class, email));

    }
}
