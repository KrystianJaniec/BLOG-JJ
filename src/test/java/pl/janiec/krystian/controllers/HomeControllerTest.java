package pl.janiec.krystian.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.janiec.krystian.controller.HomeController;
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.models.Post;
import pl.janiec.krystian.service.CategoryService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.janiec.krystian.util.TestConstants.*;
import static pl.janiec.krystian.util.TestUtil.*;

public class HomeControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        homeController = new HomeController(categoryService);

        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    public void shouldShowHomePageWithCategoryList() throws Exception {
        List<Category> categoryList = createCategoryList(new Category(), new Category());

        when(categoryService.findAll()).thenReturn(categoryList);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("categoryList", categoryList))
                .andExpect(model().attribute("view", "home/index"));
    }

    @Test
    public void shouldShowAllPostsInSelectedCategoryWithGivenId() throws Exception {
        Set<Post> posts = new HashSet<>(createPosts(new Post(), new Post()));
        Category category = createCategoryWithPosts(TEST_CATEGORY, posts);

        when(categoryService.isCategoryExist(TEST_ID)).thenReturn(true);
        when(categoryService.categoryDetails(TEST_ID)).thenReturn(category);

        mockMvc.perform(get("/category/{id}", TEST_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("category", category))
                .andExpect(model().attribute("posts", posts));

        verify(categoryService, times(1)).isCategoryExist(TEST_ID);
        verify(categoryService, times(1)).categoryDetails(TEST_ID);
    }

    @Test
    public void shouldRedirectToHomePageWhenSelectedCategoryWithGivenIdDoNotExist() throws Exception {
        when(categoryService.isCategoryExist(TEST_ID)).thenReturn(false);

        mockMvc.perform(get("/category/{id}", TEST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));

        verify(categoryService, times(1)).isCategoryExist(TEST_ID);
    }
}
