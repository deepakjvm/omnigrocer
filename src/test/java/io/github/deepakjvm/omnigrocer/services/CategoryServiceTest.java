package io.github.deepakjvm.omnigrocer.services;

import io.github.deepakjvm.omnigrocer.models.Category;
import io.github.deepakjvm.omnigrocer.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void testAddCategory() {
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category created = categoryService.addCategory("Books");
        assertNotNull(created);
        assertEquals("Books", created.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testGetCategories() {
        Category c1 = new Category();
        Category c2 = new Category();
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Category> list = categoryService.getCategories();
        assertEquals(2, list.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategory() {
        Category c = new Category();
        c.setId(4);
        when(categoryRepository.findById(4)).thenReturn(Optional.of(c));
        when(categoryRepository.findById(88)).thenReturn(Optional.empty());

        assertEquals(c, categoryService.getCategory(4));
        assertNull(categoryService.getCategory(88));
    }

    @Test
    void testUpdateCategoryFound() {
        Category c = new Category();
        c.setId(2);
        c.setName("OldName");

        when(categoryRepository.findById(2)).thenReturn(Optional.of(c));
        when(categoryRepository.save(c)).thenReturn(c);

        Category updated = categoryService.updateCategory(2, "NewName");
        assertNotNull(updated);
        assertEquals("NewName", updated.getName());
        verify(categoryRepository, times(1)).save(c);
    }

    @Test
    void testUpdateCategoryNotFound() {
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        Category updated = categoryService.updateCategory(999, "NewName");
        assertNull(updated);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testDeleteCategoryWhenExists() {
        when(categoryRepository.existsById(10)).thenReturn(true);

        Boolean result = categoryService.deleteCategory(10);
        assertTrue(result);
        verify(categoryRepository, times(1)).deleteById(10);
    }

    @Test
    void testDeleteCategoryWhenDoesNotExist() {
        when(categoryRepository.existsById(99)).thenReturn(false);

        Boolean result = categoryService.deleteCategory(99);
        assertFalse(result);
        verify(categoryRepository, never()).deleteById(anyInt());
    }
}
