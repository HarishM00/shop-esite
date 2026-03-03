package com.springboot.shop.service.product;

import com.springboot.shop.dto.ProductDto;
import com.springboot.shop.exceptions.ResourceNotFoundException;
import com.springboot.shop.model.Category;
import com.springboot.shop.model.Image;
import com.springboot.shop.model.Product;
import com.springboot.shop.repository.CategoryRepository;
import com.springboot.shop.repository.ImageRepository;
import com.springboot.shop.repository.ProductRepository;
import com.springboot.shop.requests.AddProductRequest;
import com.springboot.shop.requests.UpdateProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product("Laptop", "Dell", new BigDecimal("999.99"), 10, "A powerful laptop", category);
        product.setId(1L);
    }

    @Test
    void getProductById_existingId_returnsProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertEquals("Laptop", result.getName());
        assertEquals("Dell", result.getBrand());
    }

    @Test
    void getProductById_nonExistingId_throwsResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void addProduct_newCategoryCreated_savesProduct() {
        AddProductRequest request = new AddProductRequest();
        request.setName("Laptop");
        request.setBrand("Dell");
        request.setPrice(new BigDecimal("999.99"));
        request.setInventory(10);
        request.setDescription("A powerful laptop");
        request.setCategory(category);

        when(categoryRepository.findByName("Electronics")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.addProduct(request);

        assertEquals("Laptop", result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void addProduct_existingCategory_savesProduct() {
        AddProductRequest request = new AddProductRequest();
        request.setName("Laptop");
        request.setBrand("Dell");
        request.setPrice(new BigDecimal("999.99"));
        request.setInventory(10);
        request.setDescription("A powerful laptop");
        request.setCategory(category);

        when(categoryRepository.findByName("Electronics")).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.addProduct(request);

        assertEquals("Laptop", result.getName());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void deleteProductById_existingId_deletes() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProductById(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteProductById_nonExistingId_throwsResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProductById(99L));
    }

    @Test
    void getAllProducts_returnsList() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
    }

    @Test
    void getProductsByCategory_returnsList() {
        when(productRepository.findByCategoryName("Electronics")).thenReturn(List.of(product));

        List<Product> result = productService.getProductsByCategory("Electronics");

        assertEquals(1, result.size());
    }

    @Test
    void getProductsByBrand_returnsList() {
        when(productRepository.findByBrand("Dell")).thenReturn(List.of(product));

        List<Product> result = productService.getProductsByBrand("Dell");

        assertEquals(1, result.size());
    }

    @Test
    void countProductsByBrandAndName_returnsCount() {
        when(productRepository.countByBrandAndName("Dell", "Laptop")).thenReturn(5L);

        Long count = productService.countProductsByBrandAndName("Dell", "Laptop");

        assertEquals(5L, count);
    }

    @Test
    void convertToProductDto_convertsCorrectly() {
        ProductDto dto = new ProductDto();
        dto.setId(1L);
        dto.setName("Laptop");

        when(modelMapper.map(product, ProductDto.class)).thenReturn(dto);
        when(imageRepository.findByProductId(1L)).thenReturn(Collections.emptyList());

        ProductDto result = productService.convertToProductDto(product);

        assertEquals("Laptop", result.getName());
    }

    @Test
    void updateProductById_existingId_updatesProduct() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Laptop");
        request.setBrand("HP");
        request.setPrice(new BigDecimal("1099.99"));
        request.setDescription("Updated description");
        request.setCategory(category);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName("Electronics")).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.updateProductById(request, 1L);

        assertEquals("Updated Laptop", result.getName());
        assertEquals("HP", result.getBrand());
    }

    @Test
    void updateProductById_nonExistingId_throwsResourceNotFoundException() {
        UpdateProductRequest request = new UpdateProductRequest();
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProductById(request, 99L));
    }
}
