package project.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/admin/categories")
    public String categories(Model model){
        model.addAttribute("pageNum", 2);
        return "category/categories";
    }
    @GetMapping("/admin/getCategories")
    public @ResponseBody Page<CategoryDTO> getCategories(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page,1);
        return categoryService.getAllCategories(pageable);
    }
    @PostMapping("/admin/saveCategory")
    public @ResponseBody String saveCategory(@ModelAttribute("saveCategory") Category category){
        category.setDeleted(false);
        categoryService.saveCategory(category);
        return "success";
    }
    @GetMapping("/admin/editCategory/{id}")
    public @ResponseBody Category editCategory(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }
    @PostMapping("/admin/editCategory")
    public @ResponseBody String updateCategory(@ModelAttribute("editCategory") Category category){
        Category categoryInDb = categoryService.getCategoryById(category.getId());
        categoryInDb.setName(category.getName());
        categoryInDb.setStatus(category.getStatus());
        categoryService.saveCategory(categoryInDb);
        return "success";
    }
    @GetMapping("/admin/searchCategories")
    public @ResponseBody Page<CategoryDTO> searchCategories(@RequestParam("page")int page, @RequestParam("name")String name){
        Pageable pageable = PageRequest.of(page,1);
        return categoryService.searchCategories(name, pageable);
    }
    @GetMapping("/admin/deleteCategory/{id}")
    public @ResponseBody String deleteCategory(@PathVariable Long id){
        Category category = categoryService.getCategoryById(id);
        category.setDeleted(true);
        List<Product> products = productService.getProductsForCategory(id);
        for(Product product: products){
            product.setDeleted(true);
            productService.saveProduct(product);
        }
        categoryService.saveCategory(category);
        return "deleted";
    }
}
