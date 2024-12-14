package org.ahroo.nexus.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AuditableDTO extends BaseDTO implements Serializable {

    private LocalDateTime createdOn;

    private LocalDateTime lastUpdatedOn;

    private Boolean isDeleted;
}
