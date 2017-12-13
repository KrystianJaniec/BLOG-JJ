package pl.janiec.krystian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.janiec.krystian.dto.CategoryDTO;
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category/categoryList")
    public String categoryList(Model model) {
        List<Category> categoryList = categoryService.getList();

        model.addAttribute("view", "category/categoryList");
        model.addAttribute("categoryList", categoryList);

        return "layout";
    }

    @GetMapping("/category/createCategory")
    public String createCategory(Model model) {
        model.addAttribute("view", "category/createCategory");

        return "layout";
    }

    @PostMapping("/category/createCategory")
    public String createCategoryProcess(@Valid CategoryDTO categoryDTO) {
        if (categoryService.isCategoryEmpty(categoryDTO)) {
            return "redirect:/category/createCategory";
        }
        categoryService.createCategory(categoryDTO);
        return "redirect:/category/categoryList";
    }

    @GetMapping("/category/editCategory/{id}")
    public String editCategory(Model model, @PathVariable Long id) {
        if (!categoryService.isCategoryExist(id)) {
            return "redirect:/category/categoryList";
        }
        Category category = categoryService.categoryDetails(id);
        model.addAttribute("category", category);
        model.addAttribute("view", "category/editCategory");
        return "layout";
    }

    @PostMapping("/category/editCategory/{id}")
    public String editCategoryProcess(@Valid CategoryDTO categoryDTO, @PathVariable Long id) {
        if (!categoryService.isCategoryExist(id)) {
            return "redirect:/category/categoryList";
        }
        categoryService.editCategory(categoryDTO, id);
        return "redirect:/category/categoryList";
    }

    @GetMapping("/category/deleteCategory/{id}")
    public String deleteCategory(Model model, @PathVariable Long id) {
        if (!categoryService.isCategoryExist(id)) {
            return "redirect:/category/categoryList";
        }
        Category category = categoryService.categoryDetails(id);
        model.addAttribute("category", category);
        model.addAttribute("view", "category/deleteCategory");
        return "layout";
    }

    @PostMapping("/category/deleteCategory/{id}")
    public String deleteCategoryProcess(@PathVariable Long id) {
        if (!categoryService.isCategoryExist(id)) {
            return "redirect:/category/categoryList";
        }
        categoryService.deleteCategory(id);
        return "redirect:/category/categoryList";
    }

}
