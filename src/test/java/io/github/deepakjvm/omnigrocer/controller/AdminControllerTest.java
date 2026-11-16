package io.github.deepakjvm.omnigrocer.controller;

import io.github.deepakjvm.omnigrocer.models.Category;
import io.github.deepakjvm.omnigrocer.models.Product;
import io.github.deepakjvm.omnigrocer.models.User;
import io.github.deepakjvm.omnigrocer.services.CategoryService;
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
class AdminControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

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
    void testAdminLoginGet() throws Exception {
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminlogin"))
                .andExpect(model().attributeDoesNotExist("msg"));
    }

    @Test
    void testAdminLoginGetWithError() throws Exception {
        mockMvc.perform(get("/admin/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminlogin"))
                .andExpect(model().attributeExists("msg"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/Dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminHome"))
                .andExpect(model().attribute("admin", "admin"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminIndex() throws Exception {
        mockMvc.perform(get("/admin/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("username", "admin"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetCategories() throws Exception {
        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddCategory() throws Exception {
        mockMvc.perform(post("/admin/categories")
                        .with(csrf())
                        .param("categoryname", "Gardening"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("categories"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateCategory() throws Exception {
        Category category = categoryService.addCategory("TemporaryCat");

        mockMvc.perform(post("/admin/categories/update")
                        .with(csrf())
                        .param("categoryid", String.valueOf(category.getId()))
                        .param("categoryname", "UpdatedCat"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteCategory() throws Exception {
        Category category = categoryService.addCategory("ToDelete");

        mockMvc.perform(post("/admin/categories/delete")
                        .with(csrf())
                        .param("id", String.valueOf(category.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetProducts() throws Exception {
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddProductGet() throws Exception {
        mockMvc.perform(get("/admin/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("productsAdd"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddProductPost() throws Exception {
        Category cat = categoryService.addCategory("FruitCategory");

        mockMvc.perform(post("/admin/products/add")
                        .with(csrf())
                        .param("name", "Mango")
                        .param("categoryid", String.valueOf(cat.getId()))
                        .param("price", "5")
                        .param("weight", "200")
                        .param("quantity", "30")
                        .param("description", "Sweet ripe mango")
                        .param("productImage", "mango.png"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUpdateProductPage() throws Exception {
        Category cat = categoryService.addCategory("UpdateCat");
        Product p = new Product();
        p.setName("OldProduct");
        p.setCategory(cat);
        Product saved = productService.addProduct(p);

        mockMvc.perform(get("/admin/products/update/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("productsUpdate"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateProductPost() throws Exception {
        Category cat = categoryService.addCategory("ProdUpdateCat");
        Product p = new Product();
        p.setName("OriginalProduct");
        p.setCategory(cat);
        Product saved = productService.addProduct(p);

        mockMvc.perform(post("/admin/products/update/" + saved.getId())
                        .with(csrf())
                        .param("name", "ModifiedProduct")
                        .param("categoryid", String.valueOf(cat.getId()))
                        .param("price", "12")
                        .param("weight", "350")
                        .param("quantity", "10")
                        .param("description", "Updated description")
                        .param("productImage", "updated.png"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteProductPost() throws Exception {
        Product p = new Product();
        p.setName("DeleteMeProduct");
        Product saved = productService.addProduct(p);

        mockMvc.perform(post("/admin/products/delete")
                        .with(csrf())
                        .param("id", String.valueOf(saved.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetCustomers() throws Exception {
        mockMvc.perform(get("/admin/customers"))
                .andExpect(status().isOk())
                .andExpect(view().name("displayCustomers"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    @WithMockUser(username = "admin_profile", roles = {"ADMIN"})
    void testAdminProfileDisplay() throws Exception {
        User adminUser = new User();
        adminUser.setUsername("admin_profile");
        adminUser.setEmail("admin_profile@test.com");
        adminUser.setPassword("pass");
        adminUser.setRole("ROLE_ADMIN");
        adminUser.setAddress("HQ Suite 1");
        userService.addUser(adminUser);

        mockMvc.perform(get("/admin/profileDisplay"))
                .andExpect(status().isOk())
                .andExpect(view().name("updateProfile"))
                .andExpect(model().attribute("username", "admin_profile"))
                .andExpect(model().attribute("email", "admin_profile@test.com"));
    }

    @Test
    @WithMockUser(username = "admin_update", roles = {"ADMIN"})
    void testAdminUpdateUserProfile() throws Exception {
        User adminUser = new User();
        adminUser.setUsername("admin_update");
        adminUser.setEmail("before@admin.com");
        adminUser.setPassword("pass");
        adminUser.setRole("ROLE_ADMIN");
        User saved = userService.addUser(adminUser);

        mockMvc.perform(post("/admin/updateuser")
                        .with(csrf())
                        .param("userid", String.valueOf(saved.getId()))
                        .param("username", "admin_update_renamed")
                        .param("email", "after@admin.com")
                        .param("password", "newPass")
                        .param("address", "New HQ"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("index"));
    }
}
