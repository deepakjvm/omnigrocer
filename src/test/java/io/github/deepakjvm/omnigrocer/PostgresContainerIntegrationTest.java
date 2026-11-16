package io.github.deepakjvm.omnigrocer;

import io.github.deepakjvm.omnigrocer.models.Category;
import io.github.deepakjvm.omnigrocer.models.Product;
import io.github.deepakjvm.omnigrocer.models.User;
import io.github.deepakjvm.omnigrocer.repository.CategoryRepository;
import io.github.deepakjvm.omnigrocer.repository.ProductRepository;
import io.github.deepakjvm.omnigrocer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class PostgresContainerIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.10")
            .withDatabaseName("test_ecommerce")
            .withUsername("test_user")
            .withPassword("test_pass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testPostgresContainerIsRunning() {
        assertTrue(postgres.isRunning());
    }

    @Test
    void testUserCrudInRealPostgres() {
        User user = new User();
        user.setUsername("tc_alice");
        user.setEmail("tc_alice@example.com");
        user.setPassword("secretHash");
        user.setRole("ROLE_NORMAL");
        user.setAddress("100 Container Way");

        User saved = userRepository.save(user);
        assertTrue(saved.getId() > 0);

        Optional<User> found = userRepository.findByUsername("tc_alice");
        assertTrue(found.isPresent());
        assertEquals("tc_alice@example.com", found.get().getEmail());
        assertEquals("100 Container Way", found.get().getAddress());
    }

    @Test
    void testProductAndCategoryInRealPostgres() {
        Category category = new Category();
        category.setName("Container Produce");
        Category savedCat = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Container Apple");
        product.setPrice(10);
        product.setQuantity(20);
        product.setWeight(150);
        product.setDescription("Grown inside Docker");
        product.setImage("apple.jpg");
        product.setCategory(savedCat);

        Product savedProd = productRepository.save(product);
        assertTrue(savedProd.getId() > 0);

        Optional<Product> foundProd = productRepository.findById(savedProd.getId());
        assertTrue(foundProd.isPresent());
        assertEquals("Container Apple", foundProd.get().getName());
        assertEquals("Container Produce", foundProd.get().getCategory().getName());
    }
}
