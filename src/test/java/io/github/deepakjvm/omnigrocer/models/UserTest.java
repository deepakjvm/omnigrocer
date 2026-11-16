package io.github.deepakjvm.omnigrocer.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        user.setId(42);
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setPassword("secret123");
        user.setRole("ROLE_NORMAL");
        user.setAddress("42 Wallaby Way, Sydney");

        assertEquals(42, user.getId());
        assertEquals("john_doe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("secret123", user.getPassword());
        assertEquals("ROLE_NORMAL", user.getRole());
        assertEquals("42 Wallaby Way, Sydney", user.getAddress());
    }

    @Test
    void testUserDefaultValues() {
        User user = new User();
        assertEquals(0, user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getRole());
        assertNull(user.getAddress());
    }
}
