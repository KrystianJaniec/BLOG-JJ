package pl.janiec.krystian.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.MockitoAnnotations;
import pl.janiec.krystian.controller.PostController;
import pl.janiec.krystian.dto.PostDTO;
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.models.Post;
import pl.janiec.krystian.models.User;
import pl.janiec.krystian.service.CategoryService;
import pl.janiec.krystian.service.PostService;
import pl.janiec.krystian.service.TagService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.janiec.krystian.util.TestConstants.*;
import static pl.janiec.krystian.util.TestUtil.*;

public class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private PostController postController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        postController = new PostController(postService, categoryService, tagService);

        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    public void shouldShowCreateNewPostForm() throws Exception {
        List<Category> categoryList = createCategoryList(new Category(), new Category());

        when(categoryService.findAll()).thenReturn(categoryList);

        mockMvc.perform(get("/post/createPost"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("view", "post/createPost"))
                .andExpect(model().attribute("categoryList", categoryList))
                .andExpect(view().name("layout"));

        verify(categoryService, times(1)).findAll();
    }

    @Test
    public void shouldRedirectToHomePageWhenPostWithThisIdDoNotExist() throws Exception {
        when(postService.getPost(TEST_ID)).thenReturn(new Post());

        mockMvc.perform(get("/post/{id}", TEST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void shouldShowPostDetailsFormWhenPostWithThisIdExist() throws Exception {
        Post post = new Post(TEST_TITLE, TEST_CONTENT,
                new User(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD),
                new Category(TEST_CATEGORY),
                createTags(TEST_TAG));

        when(postService.isPostExists(TEST_ID)).thenReturn(true);
        when(postService.postDetails(TEST_ID)).thenReturn(post);

        mockMvc.perform(get("/post/{id}", TEST_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("view", "post/postDetails"))
                .andExpect(model().attribute("post", post));

        verify(postService, times(1)).isPostExists(TEST_ID);
        verify(postService, times(1)).postDetails(TEST_ID);
    }

    @Test
    public void shouldRedirectFromPostDetailsFormWHenPostWithThisDoNotExist() throws Exception {
        when(postService.isPostExists(TEST_ID)).thenReturn(false);

        mockMvc.perform(get("/post/{id}", TEST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void shouldShowPostEditFormWhenPostWithThisIdExist() throws Exception {
        Post post = new Post(TEST_TITLE, TEST_CONTENT,
                new User(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD),
                new Category(TEST_CATEGORY),
                createTags(TEST_TAG));
        List<Category> categoryList = createCategoryList(new Category(TEST_CATEGORY), new Category(TEST_NEW_CATEGORY));
        String tags = joinTags(TEST_TAG, TEST_NEW_TAG);

        when(postService.isPostExists(TEST_ID)).thenReturn(true);
        when(postService.postDetails(TEST_ID)).thenReturn(post);
        when(categoryService.findAll()).thenReturn(categoryList);
        when(tagService.joinTags(any(Post.class))).thenReturn(tags);

        mockMvc.perform(get("/post/editPost/{id}", TEST_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("view", "post/editPost"))
                .andExpect(model().attribute("post", post))
                .andExpect(model().attribute("categoryList", categoryList))
                .andExpect(model().attribute("tags", tags));

        verify(postService, times(1)).postDetails(TEST_ID);
        verify(categoryService, times(1)).findAll();
        verify(tagService, times(1)).joinTags(any(Post.class));
    }

    @Test
    public void shouldRedirectFromPostEditFormWhenPostWithThisDoNotExist() throws Exception {
        when(postService.isPostExists(TEST_ID)).thenReturn(false);

        mockMvc.perform(get("/post/editPost/{id}", TEST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void shouldRedirectToHomePageBeforeEditPostWhenPostWithIdDoNotExist() throws Exception {
        PostDTO postDTO = createPostDTO(TEST_TITLE, TEST_CONTENT, TEST_TAG, TEST_ID);

        when(postService.isPostExists(TEST_ID)).thenReturn(false);

        mockMvc.perform(post("/post/editPost/{id}", TEST_ID)
                .param("title", TEST_TITLE)
                .param("content", TEST_CONTENT)
                .param("tag", TEST_TAG)
                .param("categoryId", TEST_ID.toString())
                .sessionAttr("postDTO", postDTO)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(view().name("redirect:/"));

        verify(postService,times(1)).isPostExists(TEST_ID);
    }

    @Test
    public void shouldEditPostAndRedirectToPstPageWhenPostWithIdExist() throws Exception {
        PostDTO postDTO = createPostDTO(TEST_TITLE, TEST_CONTENT, TEST_TAG, TEST_ID);
        Post post = cretePostWithId(TEST_ID);

        when(postService.isPostExists(TEST_ID)).thenReturn(true);
        when(postService.editPost(any(),any(PostDTO.class))).thenReturn(post);

        mockMvc.perform(post("/post/editPost/{id}", TEST_ID)
                .param("title", TEST_TITLE)
                .param("content", TEST_CONTENT)
                .param("tag", TEST_TAG)
                .param("categoryId", TEST_ID.toString())
                .sessionAttr("postDTO", postDTO)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/"+TEST_ID))
                .andExpect(view().name("redirect:/post/"+TEST_ID));

        verify(postService,times(1)).isPostExists(TEST_ID);
        verify(postService,times(1)).editPost(any(),any(PostDTO.class));
    }

    @Test
    public void shouldShowLayoutAfterDeletePostWhenPostWithGivenIdExist() throws Exception {
        Post post = new Post(TEST_TITLE, TEST_CONTENT,
                new User(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD),
                new Category(TEST_CATEGORY),
                createTags(TEST_TAG));

        when(postService.isPostExists(TEST_ID)).thenReturn(true);
        when(postService.postDetails(TEST_ID)).thenReturn(post);

        mockMvc.perform(get("/post/deletePost/{id}", TEST_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("post", post))
                .andExpect(model().attribute("view", "post/deletePost"));

        verify(postService, times(1)).postDetails(TEST_ID);
    }

    @Test
    public void shouldRedirectFromDeletePostFormWhenPostWithGivenIdDoNotExist() throws Exception {
        when(postService.isPostExists(TEST_ID)).thenReturn(false);

        mockMvc.perform(get("/post/deletePost/{id}", TEST_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void shouldRedirectToHomePageBeforeDeletePostWhenPostWithIdDoNotExist()throws Exception{
        when(postService.isPostExists(TEST_ID)).thenReturn(false);

        mockMvc.perform(post("/post/deletePost/{id}",TEST_ID)
                .param("id",TEST_ID.toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(view().name("redirect:/"));

        verify(postService,times(1)).isPostExists(TEST_ID);
    }

    @Test
    public void shouldDeletePostAndRedirectToHomePageWhenPostWithIdExist()throws Exception{
        when(postService.isPostExists(TEST_ID)).thenReturn(true);

        mockMvc.perform(post("/post/deletePost/{id}",TEST_ID)
                .param("id",TEST_ID.toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(view().name("redirect:/"));

        verify(postService,times(1)).isPostExists(TEST_ID);
        verify(postService,times(1)).deletePost(TEST_ID);
    }

}
