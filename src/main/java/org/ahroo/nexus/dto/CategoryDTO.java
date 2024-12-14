package org.ahroo.nexus.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.ahroo.nexus.dto.common.BaseDTO;
import org.ahroo.nexus.entity.Category;

@Getter
@Setter
public class CategoryDTO extends BaseDTO {

    @NotBlank(message = "name required")
    private String name;

    /**
     * For same user. All fields are shown.
     */
    public static CategoryDTO fromCategory(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public Category toCategory() {
        Category category = new Category();
        category.setName(this.getName());
        return category;
    }
}
