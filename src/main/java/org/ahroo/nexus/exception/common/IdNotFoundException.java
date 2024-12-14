package org.ahroo.nexus.exception.common;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;

@Getter
public class IdNotFoundException extends EntityNotFoundException {

    protected final String id;

    protected final Class<?> entity;

    public IdNotFoundException(final String id, Class<?> clazz) {
        super(String.format("%s with id: %s is not found", clazz.getSimpleName(), id));
        this.id = id;
        this.entity = clazz;
    }

    public IdNotFoundException(final String id, Class<?> clazz, String message) {
        super(message);
        this.id = id;
        this.entity = clazz;
    }
}
