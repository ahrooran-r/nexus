package org.ahroo.nexus.util;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

/**
 * <a href="https://stackoverflow.com/a/76988608/10582056">source</a>
 */
public class ControllerUtils {

    public static String getActionUrl(Class<?> cls, String name) {
        return WebMvcLinkBuilder.linkTo(cls).slash(name).withSelfRel().getHref();
    }

}
