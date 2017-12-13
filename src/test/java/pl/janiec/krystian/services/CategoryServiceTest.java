package pl.janiec.krystian.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.janiec.krystian.dto.CategoryDTO;
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.repository.CategoryRepository;
import pl.janiec.krystian.repository.PostRepository;
import pl.janiec.krystian.service.CategoryServiceImpl;

import java.util.List;

import static pl.janiec.krystian.util.TestConstants.*;
import static pl.janiec.krystian.util.TestUtil.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        categoryService = new CategoryServiceImpl(categoryRepository, postRepository);
    }

    @Test
    public void shouldGetListOfCategories() throws Exception {
        List<Category> expectedCategoryList = createCategoryListWithId(new Category(TEST_CATEGORY),TEST_ID,
                new Category(TEST_NEW_CATEGORY),TEST_NEW_ID);

        when(categoryRepository.findAll()).thenReturn(expectedCategoryList);

        List<Category> actualCategoryList = categoryService.getList();

        assertThat(actualCategoryList.get(0).getCategoryName(), is(equalTo(TEST_CATEGORY)));
        assertThat(actualCategoryList.get(1).getCategoryName(), is(equalTo(TEST_NEW_CATEGORY)));
        assertThat(actualCategoryList.get(0).getId(), is(equalTo(TEST_ID)));
        assertThat(actualCategoryList.get(1).getId(), is(equalTo(TEST_NEW_ID)));
        assertThat(actualCategoryList.size(), is(equalTo(2)));
    }

    @Test
    public void shouldReturnTrueWhenCategoryIsEmpty() throws Exception {
        CategoryDTO categoryDTO = createCategoryDTO(null);

        assertThat(categoryService.isCategoryEmpty(categoryDTO), is(true));
    }

    @Test
    public void shouldReturnFalseWhenCategoryIsNotEmpty() throws Exception {
        CategoryDTO categoryDTO = createCategoryDTO(TEST_CATEGORY);

        assertThat(categoryService.isCategoryEmpty(categoryDTO), is(false));
    }

    @Test
    public void shouldReturnFalseWhenCategoryDoNotExists() throws Exception {
        assertThat(categoryService.isCategoryExist(TEST_ID), is(false));

        verify(categoryRepository, times(1)).exists(TEST_ID);
    }

    @Test
    public void shouldSuccessfullyCreateNewCategory() throws Exception {
        CategoryDTO categoryDTO = createCategoryDTO(TEST_NEW_CATEGORY);

        when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(new Category(TEST_NEW_CATEGORY));

        Category actualCategory = categoryService.createCategory(categoryDTO);

        assertThat(actualCategory.getCategoryName(), is(equalTo(TEST_NEW_CATEGORY)));

        verify(categoryRepository, times(1)).saveAndFlush(any(Category.class));
    }

    @Test
    public void shouldFindCategoryWithThisId() throws Exception {
        Category expectedCategory = createCategoryWithId(TEST_ID);

        when(categoryRepository.findOne(TEST_ID)).thenReturn(expectedCategory);

        Category actualCategory = categoryService.categoryDetails(TEST_ID);

        assertThat(actualCategory, is(equalTo(expectedCategory)));
        assertThat(actualCategory.getId(), is(equalTo(expectedCategory.getId())));

        verify(categoryRepository, times(1)).findOne(TEST_ID);
    }

    @Test
    public void shouldSuccessfullyEditCategoryWithThisId() throws Exception {
        Category oldCategory = new Category(TEST_CATEGORY);
        CategoryDTO categoryDTO = createCategoryDTO(TEST_NEW_CATEGORY);

        when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(oldCategory);
        when(categoryRepository.findOne(TEST_ID)).thenReturn(oldCategory);

        Category editedCategory = categoryService.editCategory(categoryDTO, TEST_ID);

        assertThat(editedCategory.getCategoryName(), is(equalTo(TEST_NEW_CATEGORY)));

        verify(categoryRepository, times(1)).saveAndFlush(any(Category.class));
    }

    @Test
    public void shouldSuccessfullyDeleteCategoryWithThisId() throws Exception {
        Category category = createCategoryWithId(TEST_ID);

        when(categoryRepository.findOne(TEST_ID)).thenReturn(category);

        categoryService.deleteCategory(TEST_ID);

        verify(postRepository, times(1)).delete(category.getPosts());
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    public void shouldFindAllCategories() throws Exception {
        List<Category> expectedCategoryList = createCategoryList(new Category(TEST_CATEGORY), new Category(TEST_NEW_CATEGORY));

        when(categoryRepository.findAll()).thenReturn(expectedCategoryList);

        List<Category> actualCategoryList = categoryService.findAll();

        assertThat(actualCategoryList, is(equalTo(expectedCategoryList)));
        assertThat(actualCategoryList.get(0).getCategoryName(), is(equalTo(TEST_CATEGORY)));
        assertThat(actualCategoryList.get(1).getCategoryName(), is(equalTo(TEST_NEW_CATEGORY)));
        assertThat(actualCategoryList.size(), is(equalTo(2)));

        verify(categoryRepository, times(1)).findAll();
    }

}
