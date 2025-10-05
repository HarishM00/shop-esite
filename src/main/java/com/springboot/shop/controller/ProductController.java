package com.springboot.shop.controller;

import com.springboot.shop.dto.ProductDto;
import com.springboot.shop.model.Product;
import com.springboot.shop.requests.AddProductRequest;
import com.springboot.shop.requests.UpdateProductRequest;
import com.springboot.shop.response.ApiResponse;
import com.springboot.shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found All Products",convertedProducts));
    }

    @GetMapping("/product/getBy/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        Product product = productService.getProductById(id);
        ProductDto productDto = productService.convertToProductDto(product);
        return ResponseEntity.ok(new ApiResponse("Found Product by Id : "+id,productDto));
    }

    @GetMapping("/getBy/{name}")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name){
        List<Product> products = productService.getProductsByName(name);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found with Name : "+name,null));
        }
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found Products by Name : "+name,convertedProducts));
    }

    @GetMapping("/getBy/{category}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category){
        List<Product> products = productService.getProductsByCategory(category);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found with Category : "+category,null));
        }
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found Products by Category : "+category,convertedProducts));
    }

    @GetMapping("/getBy/{Brand}")
    public ResponseEntity<ApiResponse> getProductsByBrand(@PathVariable String Brand){
        List<Product> products = productService.getProductsByBrand(Brand);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found with Brand : "+Brand,null));
        }
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found Products by Brand : "+Brand,convertedProducts));
    }

    @GetMapping("/getBy/Brand-and-Name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String Brand,@RequestParam String Name){
        List<Product> products = productService.getProductsByBrandAndName(Brand,Name);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found with Brand and Name : "+Brand +" and "+Name,null));
        }
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found Products by BrandAndName : "+Brand +" and "+Name,convertedProducts));
    }

    @GetMapping("/getBy/Category-and-Brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String Category,@RequestParam String Brand){
        List<Product> products = productService.getProductsByCategoryAndBrand(Category,Brand);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products found with Category and Brand : "+Category +" and "+Brand,null));
        }
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found Products by CategoryAndBrand : "+Category+" and "+Brand,convertedProducts));
    }

    @GetMapping("/countProductsBy/Brand-and-Name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String Brand,@RequestParam String Name){
        return ResponseEntity.ok(new ApiResponse("Count of Products By Brand And Name : "+Brand+" and "+Name,productService.countProductsByBrandAndName(Brand, Name)));
    }

    @PostMapping("/product/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request){
        return ResponseEntity.ok(new ApiResponse("Adding product successful",productService.addProduct(request)));
    }

    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest request){
        return ResponseEntity.ok(new ApiResponse("Updating product is success",productService.updateProductById(request,id)));
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id){
        productService.deleteProductById(id);
        return ResponseEntity.ok(new ApiResponse("Delete Success",null));
    }
}
