package org.ahroo.nexus.exception.common;

import lombok.Getter;
import org.ahroo.nexus.entity.common.BaseEntity;

@Getter
public class UpdatesProhibitedException extends RuntimeException {

    private final BaseEntity entity;

    public UpdatesProhibitedException(final BaseEntity entity) {
        super(String.format("Updates to %s are prohibited", entity.getClass().getSimpleName()));
        this.entity = entity;
    }
}
