package pl.janiec.krystian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.janiec.krystian.dto.CategoryDTO;
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.repository.CategoryRepository;
import pl.janiec.krystian.repository.PostRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    private final PostRepository postRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Category> getList() {
        return categoryRepository.findAll().stream()
                .sorted(Comparator.comparing(Category::getId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCategoryEmpty(CategoryDTO categoryDTO) {
        return StringUtils.isEmpty(categoryDTO.getCategoryName());
    }

    @Override
    public boolean isCategoryExist(Long id) {
        return categoryRepository.exists(id);
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO.getCategoryName().toUpperCase());

        return categoryRepository.saveAndFlush(category);
    }

    @Override
    public Category categoryDetails(Long id) {
        return categoryRepository.findOne(id);
    }

    @Override
    public Category editCategory(CategoryDTO categoryDTO, Long id) {
        Category category = categoryRepository.findOne(id);
        category.setCategoryName(categoryDTO.getCategoryName());

        return categoryRepository.saveAndFlush(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findOne(id);
        postRepository.delete(category.getPosts());
        categoryRepository.delete(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
