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

import java.util.*;

import pl.janiec.krystian.dto.PostDTO;
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.models.Post;
import pl.janiec.krystian.models.Tag;
import pl.janiec.krystian.models.User;
import pl.janiec.krystian.repository.CategoryRepository;
import pl.janiec.krystian.repository.PostRepository;
import pl.janiec.krystian.repository.UserRepository;
import pl.janiec.krystian.service.PostServiceImpl;
import pl.janiec.krystian.service.TagService;

import static pl.janiec.krystian.util.TestConstants.*;
import static pl.janiec.krystian.util.TestUtil.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagService tagService;

    @InjectMocks
    private PostServiceImpl postService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        postService = new PostServiceImpl(userRepository, postRepository, categoryRepository, tagService);
    }

    @Test
    public void shouldSuccessfullyCreateNewPost() throws Exception {
        PostDTO postDTO = createPostDTO(TEST_TITLE, TEST_CONTENT, TEST_TAG, TEST_CATEGORY_ID);

        when(tagService.findTagByName(any(String.class))).thenReturn(new Tag(TEST_TAG));
        when(postRepository.saveAndFlush(any(Post.class))).thenReturn(
                new Post(TEST_TITLE, TEST_CONTENT,
                        new User(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD),
                        new Category(TEST_CATEGORY),
                        createTags(TEST_TAG)));

        Post actualPost = postService.createPost(postDTO, TEST_EMAIL);

        assertThat(actualPost.getTitle(), is(equalTo(TEST_TITLE)));
        assertThat(actualPost.getContent(), is(equalTo(TEST_CONTENT)));
        assertThat(actualPost.getAuthor().getEmail(), is(equalTo(TEST_EMAIL)));
        assertThat(actualPost.getAuthor().getFullName(), is(equalTo(TEST_FULL_NAME)));
        assertThat(actualPost.getAuthor().getPassword(), is(equalTo(TEST_PASSWORD)));

        verify(postRepository, times(1)).saveAndFlush(any(Post.class));
    }

    @Test
    public void shouldFindAllPosts() throws Exception {
        List<Post> expectedPosts = createPosts(new Post(), new Post());

        when(postRepository.findAll()).thenReturn(expectedPosts);

        List<Post> actualPosts = postService.findAllPosts();

        assertThat(actualPosts, is(equalTo(expectedPosts)));
        assertThat(actualPosts.size(), is(equalTo(2)));

        verify(postRepository, times(1)).findAll();
    }

    @Test
    public void shouldShowDetailsOfPostWithThisId() throws Exception {
        Post expectedPost = new Post(TEST_TITLE, TEST_CONTENT,
                new User(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD),
                new Category(TEST_CATEGORY),
                createTags(TEST_TAG));

        when(postRepository.findOne(TEST_ID)).thenReturn(expectedPost);

        Post actualPost = postService.postDetails(TEST_ID);

        assertThat(actualPost, is(equalTo(expectedPost)));
        assertThat(actualPost.getTitle(), is(equalTo(TEST_TITLE)));
        assertThat(actualPost.getContent(), is(equalTo(TEST_CONTENT)));
        assertThat(actualPost.getAuthor().getEmail(), is(equalTo(TEST_EMAIL)));
        assertThat(actualPost.getAuthor().getFullName(), is(equalTo(TEST_FULL_NAME)));
        assertThat(actualPost.getAuthor().getPassword(), is(equalTo(TEST_PASSWORD)));

        verify(postRepository, times(1)).findOne(TEST_ID);
    }

    @Test
    public void shouldSuccessfullyFindPostWithThisId() throws Exception {
        Post expectedPost = cretePostWithId(TEST_ID);

        when(postRepository.findOne(TEST_ID)).thenReturn(expectedPost);

        Post actualPost = postService.getPost(TEST_ID);

        assertThat(actualPost, is(equalTo(expectedPost)));
        assertThat(actualPost.getId(), is(equalTo(expectedPost.getId())));

        verify(postRepository, times(1)).findOne(TEST_ID);
    }

    @Test
    public void shouldReturnNullWhenPostNotExists() throws Exception {
        Post actualPost = postService.getPost(TEST_ID);

        assertThat(actualPost, is(equalTo(null)));

        verify(postRepository, times(1)).findOne(TEST_ID);
    }

    @Test
    public void shouldReturnFalseWhenPostDoNotExists() throws Exception {
        assertThat(postService.isPostExists(TEST_ID), is(false));

        verify(postRepository, times(1)).exists(TEST_ID);
    }


    @Test
    public void shouldSuccessfullyEditPostWithGivenId() throws Exception {
        Post oldPost = new Post(TEST_TITLE, TEST_CONTENT,
                new User(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD),
                new Category(TEST_CATEGORY),
                createTags(TEST_TAG));
        PostDTO postDTO = createPostDTO(TEST_TITLE, TEST_CONTENT, TEST_TAG, TEST_CATEGORY_ID);

        when(postRepository.saveAndFlush(any(Post.class))).thenReturn(oldPost);
        when(postRepository.findOne(TEST_ID)).thenReturn(oldPost);

        Post editedPost = postService.editPost(TEST_ID, postDTO);

        assertThat(editedPost.getTitle(), is(equalTo(TEST_TITLE)));
        assertThat(editedPost.getContent(), is(equalTo(TEST_CONTENT)));
        assertThat(editedPost.getAuthor().getEmail(), is(equalTo(TEST_EMAIL)));
        assertThat(editedPost.getAuthor().getFullName(), is(equalTo(TEST_FULL_NAME)));
        assertThat(editedPost.getAuthor().getPassword(), is(equalTo(TEST_PASSWORD)));

        verify(postRepository, times(1)).saveAndFlush(any(Post.class));
    }

    @Test
    public void shouldSuccessfullyDeletePostWithThisId() throws Exception {
        Post post = cretePostWithId(TEST_ID);

        when(postRepository.findOne(TEST_ID)).thenReturn(post);

        postService.deletePost(TEST_ID);

        verify(postRepository, times(1)).delete(post);
    }

}
