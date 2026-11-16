package io.github.deepakjvm.omnigrocer.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryGettersAndSetters() {
        Category category = new Category();
        category.setId(3);
        category.setName("Beverages");

        assertEquals(3, category.getId());
        assertEquals("Beverages", category.getName());
    }

    @Test
    void testCategoryDefaultValues() {
        Category category = new Category();
        assertEquals(0, category.getId());
        assertNull(category.getName());
    }
}
