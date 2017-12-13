package pl.janiec.krystian.dto;

import org.hibernate.validator.constraints.NotBlank;

public class CategoryDTO {

    @NotBlank
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
