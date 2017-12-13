package pl.janiec.krystian.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.janiec.krystian.dto.CategoryDTO;
import pl.janiec.krystian.controller.CategoryController;
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.service.CategoryService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.janiec.krystian.util.TestConstants.*;
import static pl.janiec.krystian.util.TestUtil.*;

public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryController = new CategoryController(categoryService);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void shouldShowCategoryList() throws Exception {
        List<Category> categoryList = createCategoryList(new Category(), new Category());

        when(categoryService.getList()).thenReturn(categoryList);

        mockMvc.perform(get("/category/categoryList"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("categoryList", categoryList))
                .andExpect(model().attribute("categoryList", hasSize(categoryList.size())))
                .andExpect(model().attribute("view", "category/categoryList"));

        verify(categoryService, times(1)).getList();
    }

    @Test
    public void shouldShowCreateCategoryForm() throws Exception {
        mockMvc.perform(get("/category/createCategory"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("view", "category/createCategory"));
    }

    @Test
    public void shouldSuccessfullyCreateNewCategoryAndRedirectToCategoryListWhenCategoryWithThisIdExist() throws Exception {
        CategoryDTO categoryDTO = createCategoryDTO(TEST_CATEGORY);

        when(categoryService.isCategoryEmpty(categoryDTO)).thenReturn(false);

        mockMvc.perform(post("/category/createCategory")
                .param("categoryName", TEST_CATEGORY)
                .sessionAttr("categoryDTO", categoryDTO)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category/categoryList"))
                .andExpect(redirectedUrl("/category/categoryList"));

        verify(categoryService, times(1)).isCategoryEmpty(any(CategoryDTO.class));
        verify(categoryService, times(1)).createCategory(any(CategoryDTO.class));
    }

    @Test
    public void shouldRedirectToCategoryListBeforeCreateCategoryWhenCategoryWithThisIdDoNotExist() throws Exception {
        CategoryDTO categoryDTO = createCategoryDTO(TEST_CATEGORY);

        when(categoryService.isCategoryEmpty(categoryDTO)).thenReturn(true);

        mockMvc.perform(post("/category/createCategory")
                .param("categoryName", TEST_CATEGORY)
                .sessionAttr("categoryDTO", categoryDTO)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category/categoryList"))
                .andExpect(redirectedUrl("/category/categoryList"));

        verify(categoryService, times(1)).isCategoryEmpty(any(CategoryDTO.class));
    }

    @Test
    public void shouldShowEditCategoryFormWhenCategoryWithThisIdExist() throws Exception {
        Category category = new Category(TEST_CATEGORY);

        when(categoryService.isCategoryExist(TEST_ID)).thenReturn(true);
        when(categoryService.categoryDetails(TEST_ID)).thenReturn(category);

        mockMvc.perform(get("/category/editCategory/{id}", TEST_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("category", category))
                .andExpect(model().attribute("view", "category/editCategory"));

        verify(categoryService, times(1)).isCategoryExist(TEST_ID);
        verify(categoryService, times(1)).categoryDetails(TEST_ID);
    }

    @Test
    public void shouldRedirectFromEditCategoryFormWhenCategoryWithThisIdDoNotExist() throws Exception {
        when(categoryService.isCategoryExist(TEST_ID)).thenReturn(false);

        mockMvc.perform(get("/category/editCategory/{id}", TEST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category/categoryList"))
                .andExpect(redirectedUrl("/category/categoryList"));

        verify(categoryService, times(1)).isCategoryExist(TEST_ID);
    }

    @Test
    public void shouldSuccessfullyEditCategoryAndRedirectToCategoryList() throws Exception {
        CategoryDTO categoryDTO = createCategoryDTO(TEST_CATEGORY);

        when(categoryService.isCategoryExist(TEST_ID)).thenReturn(true);

        mockMvc.perform(post("/category/editCategory/{id}", TEST_ID)
                .param("id", TEST_ID.toString())
                .param("categoryName", TEST_CATEGORY)
                .sessionAttr("categoryDTO", categoryDTO)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category/categoryList"))
                .andExpect(redirectedUrl("/category/categoryList"));

        verify(categoryService, times(1)).isCategoryExist(TEST_ID);
    }

    @Test
    public void shouldShowDeleteCategoryFormWhenCategoryWithThisIdExist() throws Exception {
        Category category = new Category(TEST_CATEGORY);

        when(categoryService.isCategoryExist(TEST_ID)).thenReturn(true);
        when(categoryService.categoryDetails(TEST_ID)).thenReturn(category);

        mockMvc.perform(get("/category/deleteCategory/{id}", TEST_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("category", category))
                .andExpect(model().attribute("view", "category/deleteCategory"));

        verify(categoryService, times(1)).isCategoryExist(TEST_ID);
        verify(categoryService, times(1)).categoryDetails(TEST_ID);
    }

    @Test
    public void shouldRedirectFromDeleteCategoryFormWhenCategoryWithThisIdDoNotExist() throws Exception {
        when(categoryService.isCategoryExist(TEST_ID)).thenReturn(false);

        mockMvc.perform(get("/category/deleteCategory/{id}", TEST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category/categoryList"))
                .andExpect(redirectedUrl("/category/categoryList"));

        verify(categoryService, times(1)).isCategoryExist(TEST_ID);
    }

    @Test
    public void shouldRedirectToCategoryListBeforeDeleteCategoryWhenCategoryWithThisIdDoNotExist() throws Exception {
        when(categoryService.isCategoryExist(TEST_ID)).thenReturn(false);

        mockMvc.perform(post("/category/deleteCategory/{id}", TEST_ID)
                .param("id", TEST_ID.toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category/categoryList"))
                .andExpect(redirectedUrl("/category/categoryList"));

        verify(categoryService, times(1)).isCategoryExist(TEST_ID);
    }

    @Test
    public void shouldSuccessfullyDeleteCategoryWithThisId() throws Exception {
        when(categoryService.isCategoryExist(TEST_ID)).thenReturn(true);

        mockMvc.perform(post("/category/deleteCategory/{id}", TEST_ID)
                .param("id", TEST_ID.toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category/categoryList"))
                .andExpect(redirectedUrl("/category/categoryList"));

        verify(categoryService, times(1)).isCategoryExist(TEST_ID);
        verify(categoryService, times(1)).deleteCategory(TEST_ID);
    }
}
