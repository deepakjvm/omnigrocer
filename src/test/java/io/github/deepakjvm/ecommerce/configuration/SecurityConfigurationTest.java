package io.github.deepakjvm.ecommerce.configuration;

import io.github.deepakjvm.ecommerce.models.User;
import io.github.deepakjvm.ecommerce.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SecurityConfigurationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void testAnonymousAccessToAdminRedirectsToAdminLogin() throws Exception {
        mockMvc.perform(get("/admin/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    @Test
    void testAnonymousAccessToUserRootRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testAnonymousAccessToLoginPermitted() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void testAnonymousAccessToRegisterPermitted() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "regular_user", roles = {"USER"})
    void testUserRoleCannotAccessAdminArea() throws Exception {
        mockMvc.perform(get("/admin/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin_user", roles = {"ADMIN"})
    void testAdminRoleCanAccessAdminArea() throws Exception {
        mockMvc.perform(get("/admin/"))
                .andExpect(status().isOk());
    }

    @Test
    void testUserDetailsServiceLoadsAdminRole() {
        User admin = new User();
        String username = "sec_admin_" + System.currentTimeMillis();
        admin.setUsername(username);
        admin.setPassword("password");
        admin.setRole("ROLE_ADMIN");
        userService.addUser(admin);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testUserDetailsServiceLoadsUserRole() {
        User normalUser = new User();
        String username = "sec_user_" + System.currentTimeMillis();
        normalUser.setUsername(username);
        normalUser.setPassword("password");
        normalUser.setRole("ROLE_NORMAL");
        userService.addUser(normalUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testUserDetailsServiceThrowsWhenNotFound() {
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("non_existent_security_user_" + System.currentTimeMillis()));
    }
}
