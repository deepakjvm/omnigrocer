package io.github.deepakjvm.omnigrocer.controller;

import io.github.deepakjvm.omnigrocer.models.Product;
import io.github.deepakjvm.omnigrocer.models.User;
import io.github.deepakjvm.omnigrocer.services.ProductService;
import io.github.deepakjvm.omnigrocer.services.UserService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void testRegisterGet() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testLoginGetWithoutError() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("userLogin"))
                .andExpect(model().attributeDoesNotExist("msg"));
    }

    @Test
    void testLoginGetWithError() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("userLogin"))
                .andExpect(model().attributeExists("msg"));
    }

    @Test
    void testNewUserRegisterSuccess() throws Exception {
        String uniqueUsername = "new_user_" + System.currentTimeMillis();

        mockMvc.perform(post("/newuserregister")
                        .with(csrf())
                        .param("username", uniqueUsername)
                        .param("email", uniqueUsername + "@test.com")
                        .param("password", "secret")
                        .param("address", "123 Test St"))
                .andExpect(status().isOk())
                .andExpect(view().name("userLogin"));
    }

    @Test
    void testNewUserRegisterDuplicateUsername() throws Exception {
        User existing = new User();
        String username = "existing_" + System.currentTimeMillis();
        existing.setUsername(username);
        existing.setEmail(username + "@test.com");
        existing.setPassword("pass");
        userService.addUser(existing);

        mockMvc.perform(post("/newuserregister")
                        .with(csrf())
                        .param("username", username)
                        .param("email", "other@test.com")
                        .param("password", "secret")
                        .param("address", "321 Test St"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("msg"))
                .andExpect(model().attribute("msg", containsString("is taken")));
    }

    @Test
    @WithMockUser(username = "testbuyer", roles = {"USER"})
    void testIndexPageWithProducts() throws Exception {
        Product p = new Product();
        p.setName("IndexTestProduct");
        productService.addProduct(p);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    @WithMockUser(username = "testbuyer", roles = {"USER"})
    void testUserProductsCatalog() throws Exception {
        Product p = new Product();
        p.setName("CatalogTestProduct");
        productService.addProduct(p);

        mockMvc.perform(get("/user/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("uproduct"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    @WithMockUser(username = "testbuyer", roles = {"USER"})
    void testBuyPage() throws Exception {
        mockMvc.perform(get("/buy"))
                .andExpect(status().isOk())
                .andExpect(view().name("buy"));
    }

    @Test
    @WithMockUser(username = "profile_user", roles = {"USER"})
    void testProfileDisplayWhenUserExists() throws Exception {
        User user = new User();
        user.setUsername("profile_user");
        user.setEmail("profile@test.com");
        user.setPassword("secret");
        user.setAddress("777 Parkway");
        userService.addUser(user);

        mockMvc.perform(get("/profileDisplay"))
                .andExpect(status().isOk())
                .andExpect(view().name("updateProfile"))
                .andExpect(model().attribute("username", "profile_user"))
                .andExpect(model().attribute("email", "profile@test.com"));
    }

    @Test
    @WithMockUser(username = "update_user", roles = {"USER"})
    void testUpdateUserProfile() throws Exception {
        User user = new User();
        user.setUsername("update_user");
        user.setEmail("before@test.com");
        user.setPassword("secret");
        user.setAddress("Old Address");
        User saved = userService.addUser(user);

        mockMvc.perform(post("/updateuser")
                        .with(csrf())
                        .param("userid", String.valueOf(saved.getId()))
                        .param("username", "update_user_renamed")
                        .param("email", "after@test.com")
                        .param("password", "newSecret")
                        .param("address", "New Address"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
