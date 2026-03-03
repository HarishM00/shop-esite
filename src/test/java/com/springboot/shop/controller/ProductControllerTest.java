package com.springboot.shop.controller;

import com.springboot.shop.dto.ProductDto;
import com.springboot.shop.model.Category;
import com.springboot.shop.model.Product;
import com.springboot.shop.service.product.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IProductService productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product("Laptop", "Dell", new BigDecimal("999.99"), 10, "A powerful laptop", category);
        product.setId(1L);

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Laptop");
        productDto.setBrand("Dell");
        productDto.setPrice(new BigDecimal("999.99"));
        productDto.setCategory(category);
    }

    @Test
    void getAllProducts_returnsOk() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(product));
        when(productService.getConvertedProducts(anyList())).thenReturn(List.of(productDto));

        mockMvc.perform(get("/api/v1/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Found All Products"))
                .andExpect(jsonPath("$.data[0].name").value("Laptop"));
    }

    @Test
    void getProductsByName_found_returnsOk() throws Exception {
        when(productService.getProductsByName("Laptop")).thenReturn(List.of(product));
        when(productService.getConvertedProducts(anyList())).thenReturn(List.of(productDto));

        mockMvc.perform(get("/api/v1/products/getByName/Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Found Products by Name : Laptop"));
    }

    @Test
    void getProductsByName_notFound_returns404() throws Exception {
        when(productService.getProductsByName("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/products/getByName/Unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Products found with Name : Unknown"));
    }

    @Test
    void getProductsByCategory_found_returnsOk() throws Exception {
        when(productService.getProductsByCategory("Electronics")).thenReturn(List.of(product));
        when(productService.getConvertedProducts(anyList())).thenReturn(List.of(productDto));

        mockMvc.perform(get("/api/v1/products/getByCategory/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Found Products by Category : Electronics"));
    }

    @Test
    void getProductsByBrand_found_returnsOk() throws Exception {
        when(productService.getProductsByBrand("Dell")).thenReturn(List.of(product));
        when(productService.getConvertedProducts(anyList())).thenReturn(List.of(productDto));

        mockMvc.perform(get("/api/v1/products/getByBrand/Dell"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Found Products by Brand : Dell"));
    }

    @Test
    void getProductsByBrandAndName_found_returnsOk() throws Exception {
        when(productService.getProductsByBrandAndName("Dell", "Laptop")).thenReturn(List.of(product));
        when(productService.getConvertedProducts(anyList())).thenReturn(List.of(productDto));

        mockMvc.perform(get("/api/v1/products/getBy/Brand-and-Name")
                        .param("brand", "Dell")
                        .param("name", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Found Products by BrandAndName : Dell and Laptop"));
    }

    @Test
    void countProductsByBrandAndName_returnsCount() throws Exception {
        when(productService.countProductsByBrandAndName("Dell", "Laptop")).thenReturn(5L);

        mockMvc.perform(get("/api/v1/products/countProductsBy/Brand-and-Name")
                        .param("brand", "Dell")
                        .param("name", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(5));
    }
}
