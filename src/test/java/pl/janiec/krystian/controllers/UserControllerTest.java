package pl.janiec.krystian.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.janiec.krystian.controller.UserController;
import pl.janiec.krystian.dto.UserDTO;
import pl.janiec.krystian.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.janiec.krystian.util.TestConstants.*;
import static pl.janiec.krystian.util.TestUtil.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userService);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldShowRegisterNewUserForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("view", "user/register"));
    }

    @Test
    public void shouldCreateNewUserAndRedirectToLoginFormWhenPasswordAndConfirmPasswordAreEqual() throws Exception {
        UserDTO userDTO = createUserDTO(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD, TEST_CONFIRM_PASSWORD);

        mockMvc.perform(post("/register")
                .param("email", TEST_EMAIL)
                .param("fullName", TEST_FULL_NAME)
                .param("password", TEST_PASSWORD)
                .param("confirmPassword", TEST_CONFIRM_PASSWORD)
                .sessionAttr("userDTO", userDTO)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"));

        verify(userService, times(1)).create(any(UserDTO.class));
    }

    @Test
    public void shouldRedirectToRegisterFormBeforeUserCreateWhenPasswordAndConfirmPasswordAreNotEqual() throws Exception {
        UserDTO userDTO = createUserDTO(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD, TEST_BAD_PASSWORD);

        mockMvc.perform(post("/register")
                .param("email", TEST_EMAIL)
                .param("fullName", TEST_FULL_NAME)
                .param("password", TEST_PASSWORD)
                .param("confirmPassword", TEST_BAD_PASSWORD)
                .sessionAttr("userDTO", userDTO)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(view().name("redirect:/register"));
    }

    @Test
    public void shouldShowLoginNewUserForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("view", "user/login"));
    }

    @Test
    public void shouldShowLoggedUserProfilePage() throws Exception {
        mockMvc.perform(get("/profile").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("view", "user/profile"));

        verify(userService, times(1)).profile(any(Authentication.class));
    }

}
