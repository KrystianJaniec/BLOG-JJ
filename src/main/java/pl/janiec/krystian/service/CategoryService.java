package pl.janiec.krystian.service;

import pl.janiec.krystian.dto.CategoryDTO;
import pl.janiec.krystian.models.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getList();

    boolean isCategoryEmpty(CategoryDTO categoryDTO);

    boolean isCategoryExist(Long id);

    Category createCategory(CategoryDTO categoryDTO);

    Category categoryDetails(Long id);

    Category editCategory(CategoryDTO categoryDTO, Long id);

    void deleteCategory(Long id);

    List<Category> findAll();
}
