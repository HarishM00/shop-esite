package com.springboot.shop.service.category;

import com.springboot.shop.exceptions.AlreadyExistsException;
import com.springboot.shop.exceptions.ResourceNotFoundException;
import com.springboot.shop.model.Category;
import com.springboot.shop.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
    }

    @Test
    void getCategoryById_existingId_returnsCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1L);

        assertEquals("Electronics", result.getName());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryById_nonExistingId_throwsResourceNotFoundException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void getCategoryByName_returnsCategory() {
        when(categoryRepository.findByName("Electronics")).thenReturn(category);

        Category result = categoryService.getCategoryByName("Electronics");

        assertEquals(1L, result.getId());
    }

    @Test
    void addCategory_newCategory_savesAndReturns() {
        when(categoryRepository.existsByName("Electronics")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.addCategory(category);

        assertEquals("Electronics", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void addCategory_duplicateName_throwsAlreadyExistsException() {
        when(categoryRepository.existsByName("Electronics")).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> categoryService.addCategory(category));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_existingId_updatesAndReturns() {
        Category updated = new Category();
        updated.setName("Clothing");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        Category result = categoryService.updateCategory(updated, 1L);

        assertEquals("Clothing", result.getName());
    }

    @Test
    void deleteCategory_existingId_deletes() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteCategory_nonExistingId_throwsResourceNotFoundException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(99L));
    }

    @Test
    void getAllCategory_returnsList() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.getAllCategory();

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }
}
