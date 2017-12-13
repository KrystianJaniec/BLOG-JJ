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
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.models.Post;
import pl.janiec.krystian.models.Tag;
import pl.janiec.krystian.models.User;
import pl.janiec.krystian.repository.TagRepository;
import pl.janiec.krystian.service.TagServiceImpl;

import java.util.HashSet;

import static pl.janiec.krystian.util.TestConstants.*;
import static pl.janiec.krystian.util.TestUtil.*;

public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        tagService = new TagServiceImpl(tagRepository);
    }

    @Test
    public void shouldSuccessfullyFindTagsByTagName() throws Exception {
        Tag tag = new Tag(TEST_TAG);

        when(tagRepository.findByTagName(TEST_TAG)).thenReturn(tag);

        HashSet<Tag> actualTags = tagService.findTagsByTagName(TEST_TAG);

        assertThat(actualTags.size(), is(equalTo(1)));
        assertThat(actualTags.contains(tag), is(true));

        verify(tagRepository, times(1)).findByTagName(TEST_TAG);
    }

    @Test
    public void shouldSuccessfullyFindTagByName() throws Exception {
        Tag expectedTag = new Tag(TEST_TAG);

        when(tagRepository.findByTagName(any())).thenReturn(expectedTag);

        Tag actualTag = tagService.findTagByName(TEST_TAG);

        assertThat(actualTag, is(equalTo(expectedTag)));
        assertThat(actualTag.getTagName(), is(equalTo(TEST_TAG)));

        verify(tagRepository, times(1)).findByTagName(TEST_TAG);
    }

    @Test
    public void shouldJoinTagsFromPosts() throws Exception {
        String expectedJoin = String.join(",", TEST_NEW_TAG, TEST_TAG);

        Post post = new Post(TEST_TITLE, TEST_CONTENT,
                new User(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD),
                new Category(TEST_CATEGORY),
                createTags(TEST_NEW_TAG, TEST_TAG));

        String actualJoin = tagService.joinTags(post);

        assertThat(actualJoin, is(equalTo(expectedJoin)));
    }
}
