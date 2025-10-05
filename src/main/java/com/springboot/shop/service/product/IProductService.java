package com.springboot.shop.service.product;

import com.springboot.shop.dto.ProductDto;
import com.springboot.shop.model.Product;
import com.springboot.shop.requests.AddProductRequest;
import com.springboot.shop.requests.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest product);
    Product getProductById(Long productId);
    Product updateProductById(UpdateProductRequest product, Long productId);
    void deleteProductById(Long productId);

    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToProductDto(Product product);
}
