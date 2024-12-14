package org.ahroo.nexus.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.ahroo.nexus.dto.common.AuditableDTO;
import org.ahroo.nexus.entity.Category;
import org.ahroo.nexus.entity.User;
import org.ahroo.nexus.validation.annotation.Password;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDTO extends AuditableDTO {

    @NotBlank(message = "name required")
    private String name;

    @NotBlank(message = "email required")
    @Email
    private String email;

    @Password
    private String password;

    private String description;

    @NotBlank(message = "country required")
    private String country;

    private Boolean isEnabled;

    private Set<String> categories;

    /**
     * For same user. All fields are shown.
     */
    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setDescription(user.getDescription());
        userDTO.setCreatedOn(user.getCreatedOn());
        userDTO.setLastUpdatedOn(user.getLastUpdatedOn());

        if (null != user.getCategories() && !user.getCategories().isEmpty()) {
            var subscribed = user.getCategories().stream().map(Category::getName).collect(Collectors.toCollection(TreeSet::new));
            userDTO.setCategories(subscribed);
        }

        userDTO.setIsEnabled(user.getIsEnabled());
        userDTO.setIsDeleted(user.getIsDeleted());
        return userDTO;
    }

    /**
     * For external users. Limited fields are shown.
     */
    public static UserDTO fromUserPublic(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setDescription(user.getDescription());
        return userDTO;
    }

    public User toUser() {
        User user = new User();
        user.setEmail(this.getEmail());
        user.setName(this.getName());
        user.setPassword(this.getPassword());
        user.setDescription(this.getDescription());
        user.setIsDeleted(false);
        return user;
    }
}
