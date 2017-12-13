package pl.janiec.krystian.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.janiec.krystian.dto.UserDTO;
import pl.janiec.krystian.models.User;
import pl.janiec.krystian.repository.UserRepository;
import pl.janiec.krystian.repository.UserRoleRepository;
import pl.janiec.krystian.service.UserServiceImpl;

import static org.mockito.Mockito.*;
import static pl.janiec.krystian.util.TestConstants.*;
import static pl.janiec.krystian.util.TestUtil.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userService = new UserServiceImpl(userRepository, roleRepository);
    }

    @Test
    public void shouldSuccessfullyCreateNewUser() throws Exception {
        UserDTO userDTO = createUserDTO(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD);

        when(userRepository.saveAndFlush(any(User.class))).thenReturn(
                new User(TEST_EMAIL, TEST_FULL_NAME, TEST_PASSWORD));

        User user = userService.create(userDTO);

        assertThat(user.getEmail(), is(equalTo(TEST_EMAIL)));
        assertThat(user.getFullName(), is(equalTo(TEST_FULL_NAME)));
        assertThat(user.getPassword(), is(equalTo(TEST_PASSWORD)));
    }

    @Test
    public void shouldSuccessfullyFindUserByEmail() throws Exception {
        User user = new User();

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(user);

        assertThat(userService.emailExists(TEST_EMAIL), is(equalTo(true)));
        assertThat(userService.findByEmail(TEST_EMAIL), is(equalTo(user)));

        verify(userRepository, atLeastOnce()).findByEmail(Matchers.anyString());
    }

    @Test
    public void shouldReturnNullWhenUserWithThisEmailNotExists() throws Exception {
        assertThat(userService.emailExists(TEST_EMAIL), is(equalTo(false)));
        assertThat(userService.findByEmail(TEST_EMAIL), is(equalTo(null)));

        verify(userRepository, atLeastOnce()).findByEmail(Matchers.anyString());
    }


}
