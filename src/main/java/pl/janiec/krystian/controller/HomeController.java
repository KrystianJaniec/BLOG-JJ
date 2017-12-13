package pl.janiec.krystian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.models.Post;
import pl.janiec.krystian.service.CategoryService;
import pl.janiec.krystian.service.PostService;

import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    private final CategoryService categoryService;

    @Autowired
    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Category> categoryList = categoryService.findAll();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("view", "home/index");
        return "layout";
    }

    @GetMapping("category/{id}")
    public String postsByCategory(Model model, @PathVariable Long id) {
        if (!categoryService.isCategoryExist(id)) {
            return "redirect:/";
        }
        Category category = categoryService.categoryDetails(id);
        Set<Post> posts = category.getPosts();

        model.addAttribute("category", category);
        model.addAttribute("posts", posts);
        model.addAttribute("view", "home/postsByCategory");

        return "layout";
    }

}
