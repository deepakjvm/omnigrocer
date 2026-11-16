package io.github.deepakjvm.omnigrocer.services;

import io.github.deepakjvm.omnigrocer.models.User;
import io.github.deepakjvm.omnigrocer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void testGetUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getUsers();
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testAddUserEncodesPlainTextPassword() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("plainPass123");

        when(passwordEncoder.encode("plainPass123")).thenReturn("$2a$10$encodedHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.addUser(user);
        assertEquals("$2a$10$encodedHash", saved.getPassword());
        verify(passwordEncoder, times(1)).encode("plainPass123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAddUserDoesNotReEncodeBcryptPassword() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("$2a$10$alreadyEncodedHash");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.addUser(user);
        assertEquals("$2a$10$alreadyEncodedHash", saved.getPassword());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void testAddUserThrowsIllegalStateExceptionOnDataIntegrityViolation() {
        User user = new User();
        user.setUsername("duplicate_user");
        user.setPassword("pass");

        when(passwordEncoder.encode("pass")).thenReturn("$2a$10$hash");
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("duplicate key"));

        assertThrows(IllegalStateException.class, () -> userService.addUser(user));
    }

    @Test
    void testCheckUserExists() {
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);
        when(userRepository.existsByUsername("newUser")).thenReturn(false);

        assertTrue(userService.checkUserExists("existingUser"));
        assertFalse(userService.checkUserExists("newUser"));
    }

    @Test
    void testGetUserByUsernameMigratesPlainTextPassword() {
        User user = new User();
        user.setUsername("legacyUser");
        user.setPassword("oldPlainText");

        when(userRepository.findByUsername("legacyUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("oldPlainText")).thenReturn("$2a$10$newHash");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.getUserByUsername("legacyUser");
        assertNotNull(result);
        assertEquals("$2a$10$newHash", result.getPassword());
        verify(passwordEncoder, times(1)).encode("oldPlainText");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserByUsernameNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        User result = userService.getUserByUsername("unknown");
        assertNull(result);
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(5);
        when(userRepository.findById(5)).thenReturn(Optional.of(user));
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertEquals(user, userService.getUserById(5));
        assertNull(userService.getUserById(99));
    }

    @Test
    void testUpdateUserProfileSuccessWithNewPassword() {
        User user = new User();
        user.setId(1);
        user.setUsername("oldName");
        user.setEmail("old@example.com");
        user.setAddress("Old Address");
        user.setPassword("$2a$10$oldHash");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("$2a$10$newHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUserProfile(1, "newName", "new@example.com", "newPassword", "New Address");

        assertNotNull(updated);
        assertEquals("newName", updated.getUsername());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals("New Address", updated.getAddress());
        assertEquals("$2a$10$newHash", updated.getPassword());
    }

    @Test
    void testUpdateUserProfileKeepsExistingPasswordWhenEmpty() {
        User user = new User();
        user.setId(1);
        user.setPassword("$2a$10$keepThisHash");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUserProfile(1, "name", "email", "   ", "address");

        assertNotNull(updated);
        assertEquals("$2a$10$keepThisHash", updated.getPassword());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void testUpdateUserProfileUserNotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        User result = userService.updateUserProfile(999, "name", "email", "pass", "address");
        assertNull(result);
        verify(userRepository, never()).save(any());
    }
}
