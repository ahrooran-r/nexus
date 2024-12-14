package org.ahroo.nexus.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class BaseDTO implements Serializable {
    private Integer id;
}
