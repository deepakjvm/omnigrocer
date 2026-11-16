package io.github.deepakjvm.omnigrocer.repository;

import io.github.deepakjvm.omnigrocer.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindByUsername() {
        User user = new User();
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        user.setPassword("password123");
        user.setRole("ROLE_NORMAL");
        user.setAddress("123 Maple Street");

        User saved = userRepository.save(user);
        assertTrue(saved.getId() > 0);

        Optional<User> found = userRepository.findByUsername("alice");
        assertTrue(found.isPresent());
        assertEquals("alice@example.com", found.get().getEmail());
        assertEquals("123 Maple Street", found.get().getAddress());
    }

    @Test
    void testExistsByUsername() {
        User user = new User();
        user.setUsername("bob");
        user.setEmail("bob@example.com");
        user.setPassword("secret");
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);

        assertTrue(userRepository.existsByUsername("bob"));
        assertFalse(userRepository.existsByUsername("nonexistent_user"));
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setUsername("charlie");
        user.setEmail("charlie@example.com");
        User saved = userRepository.save(user);

        userRepository.deleteById(saved.getId());
        assertFalse(userRepository.findById(saved.getId()).isPresent());
    }
}
