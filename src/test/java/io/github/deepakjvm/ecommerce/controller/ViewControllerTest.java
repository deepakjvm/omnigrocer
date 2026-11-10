package io.github.deepakjvm.ecommerce.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ViewControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void testUserLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User Login")));
    }

    @Test
    void testRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sign Up Now")));
    }

    @Test
    void testAdminLoginPage() throws Exception {
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Admin Login")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome Back, Admin")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminCategoriesPage() throws Exception {
        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Category Name")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminProductsPage() throws Exception {
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Product Management")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUserIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome to OmniGrocer")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUserProductsCatalog() throws Exception {
        mockMvc.perform(get("/user/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Product Catalog")));
    }
}
