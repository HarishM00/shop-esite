package com.springboot.shop.controller;

import com.springboot.shop.model.Category;
import com.springboot.shop.response.ApiResponse;
import com.springboot.shop.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        List<Category> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(new ApiResponse("Found All Categories",categories));
    }

    @PostMapping("/category/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        Category newCategory = categoryService.addCategory(category);
        return ResponseEntity.ok(new ApiResponse("Adding category successful",newCategory));
    }

    @GetMapping("/category/getById/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new ApiResponse("Found Category by Id : "+id,category));
    }
    @GetMapping("/category/getByName/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        Category category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(new ApiResponse("Found Category by Name : "+name,category));
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse("Delete Success",null));
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category){
        Category updatedCategory = categoryService.updateCategory(category,id);
        return ResponseEntity.ok(new ApiResponse("Update Success",updatedCategory));
    }
}
