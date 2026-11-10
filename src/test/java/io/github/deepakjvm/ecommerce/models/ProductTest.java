package io.github.deepakjvm.ecommerce.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductGettersAndSetters() {
        Product product = new Product();
        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        User customer = new User();
        customer.setId(5);
        customer.setUsername("seller1");

        product.setId(10);
        product.setName("Smartphone");
        product.setImage("phone.jpg");
        product.setCategory(category);
        product.setQuantity(25);
        product.setPrice(799);
        product.setWeight(180);
        product.setDescription("Latest flagship phone");
        product.setCustomer(customer);

        assertEquals(10, product.getId());
        assertEquals("Smartphone", product.getName());
        assertEquals("phone.jpg", product.getImage());
        assertEquals(category, product.getCategory());
        assertEquals("Electronics", product.getCategory().getName());
        assertEquals(25, product.getQuantity());
        assertEquals(799, product.getPrice());
        assertEquals(180, product.getWeight());
        assertEquals("Latest flagship phone", product.getDescription());
        assertEquals(customer, product.getCustomer());
        assertEquals("seller1", product.getCustomer().getUsername());
    }

    @Test
    void testProductDefaultValues() {
        Product product = new Product();
        assertEquals(0, product.getId());
        assertNull(product.getName());
        assertNull(product.getImage());
        assertNull(product.getCategory());
        assertEquals(0, product.getQuantity());
        assertEquals(0, product.getPrice());
        assertEquals(0, product.getWeight());
        assertNull(product.getDescription());
        assertNull(product.getCustomer());
    }
}
