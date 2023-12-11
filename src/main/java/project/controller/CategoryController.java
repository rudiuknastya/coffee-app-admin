package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.entity.Admin;
import project.model.CategoryDTO;
import project.entity.Category;
import project.entity.Product;
import project.service.AdminService;
import project.service.CategoryService;
import project.service.ProductService;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final AdminService adminService;

    public CategoryController(CategoryService categoryService, AdminService adminService) {
        this.categoryService = categoryService;
        this.adminService = adminService;
    }

    private int pageSize = 5;
    @GetMapping("/categories")
    public String categories(Model model){
        model.addAttribute("pageNum", 2);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "category/categories";
    }
    @GetMapping("/getCategories")
    public @ResponseBody Page<CategoryDTO> getCategories(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page,pageSize);
        return categoryService.getAllCategories(pageable);
    }
    @PostMapping("/saveCategory")
    public @ResponseBody ResponseEntity<?> saveCategory(@Valid @ModelAttribute("saveCategory") Category category){
        category.setDeleted(false);
        categoryService.saveCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/editCategory/{id}")
    public @ResponseBody Category editCategory(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }
    @PostMapping("/editCategory")
    public @ResponseBody ResponseEntity<?> updateCategory(@Valid @ModelAttribute("editCategory") Category category){
        categoryService.updateCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/searchCategories")
    public @ResponseBody Page<CategoryDTO> searchCategories(@RequestParam("page")int page, @RequestParam("name")String name){
        Pageable pageable = PageRequest.of(page,pageSize);
        return categoryService.searchCategories(name, pageable);
    }
    @GetMapping("/deleteCategory/{id}")
    public @ResponseBody ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
