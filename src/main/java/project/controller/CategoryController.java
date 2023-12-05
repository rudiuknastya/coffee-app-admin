package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.model.CategoryDTO;
import project.entity.Category;
import project.entity.Product;
import project.service.CategoryService;
import project.service.ProductService;

import java.util.List;

@Controller
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    private int pageSize = 1;
    @GetMapping("/categories")
    public String categories(Model model){
        model.addAttribute("pageNum", 2);
        return "category/categories";
    }
    @GetMapping("/getCategories")
    public @ResponseBody Page<CategoryDTO> getCategories(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page,pageSize);
        return categoryService.getAllCategories(pageable);
    }
    @PostMapping("/saveCategory")
    public @ResponseBody List<FieldError> saveCategory(@Valid @ModelAttribute("saveCategory") Category category, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return bindingResult.getFieldErrors();
        }
        category.setDeleted(false);
        categoryService.saveCategory(category);
        return null;
    }
    @GetMapping("/editCategory/{id}")
    public @ResponseBody Category editCategory(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }
    @PostMapping("/editCategory")
    public @ResponseBody List<FieldError> updateCategory(@Valid @ModelAttribute("editCategory") Category category, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return bindingResult.getFieldErrors();
        }
        categoryService.updateCategory(category);
        return null;
    }
    @GetMapping("/searchCategories")
    public @ResponseBody Page<CategoryDTO> searchCategories(@RequestParam("page")int page, @RequestParam("name")String name){
        Pageable pageable = PageRequest.of(page,pageSize);
        return categoryService.searchCategories(name, pageable);
    }
    @GetMapping("/deleteCategory/{id}")
    public @ResponseBody ResponseEntity deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
