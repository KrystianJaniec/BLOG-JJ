package pl.janiec.krystian.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.janiec.krystian.controller.TagController;
import pl.janiec.krystian.models.Tag;
import pl.janiec.krystian.service.TagService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.janiec.krystian.util.TestConstants.*;

public class TagControllerTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tagController = new TagController(tagService);

        mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
    }

    @Test
    public void shouldShowPostsWithThisTagWhenTagNameIsNotNull() throws Exception {
        Tag tag = new Tag(TEST_TAG);

        when(tagService.findTagByName(TEST_TAG)).thenReturn(tag);

        mockMvc.perform(get("/tag/{tagName}", TEST_TAG))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("tag", tag))
                .andExpect(model().attribute("view", "tag/postsWithTag"));

        verify(tagService, times(1)).findTagByName(TEST_TAG);
    }

    @Test
    public void shouldRedirectToHomePageWhenTagWithThisISNull() throws Exception {
        Tag tag = null;

        when(tagService.findTagByName(TEST_TAG)).thenReturn(tag);

        mockMvc.perform(get("/tag/{tagName}", TEST_TAG))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));

        verify(tagService, times(1)).findTagByName(TEST_TAG);
    }
}
