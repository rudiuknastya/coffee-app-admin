package project.controller;

import jakarta.validation.Valid;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.multipart.MultipartFile;
import project.entity.Admin;
import project.model.CategoryNameDTO;
import project.model.productModel.ProductDTO;
import project.entity.AdditiveType;
import project.entity.Product;
import project.model.productModel.ProductRequest;
import project.model.productModel.ProductResponse;
import project.service.AdditiveTypeService;
import project.service.AdminService;
import project.service.CategoryService;
import project.service.ProductService;
import project.validators.fileExtentionValidator.ValidFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final AdminService adminService;

    public ProductController(ProductService productService, CategoryService categoryService, AdminService adminService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.adminService = adminService;
    }

    private int pageSize = 5;
    @GetMapping("/products")
    public String products(Model model){
        model.addAttribute("pageNum", 3);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "product/products";
    }
    @GetMapping("/getProducts")
    public @ResponseBody Page<ProductDTO> getProducts(@RequestParam("page")int page,@RequestParam("size")int size){
        Pageable pageable = PageRequest.of(page, size);
        return productService.getProducts(pageable);
    }
    @GetMapping("/getCategoriesForProducts")
    public @ResponseBody Page<CategoryNameDTO> getCategoriesForProducts(@RequestParam(value = "search", required = false)String name, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1, pageSize);
        return categoryService.getCategoriesName(pageable, name);
    }
    @GetMapping("/getCategoryForProduct/{id}")
    public @ResponseBody CategoryNameDTO getCategoryForProduct(@PathVariable Long id){
        return categoryService.getCategoryNameDTOById(id);
    }
    @GetMapping("/searchProduct")
    public @ResponseBody Page<ProductDTO> searchProduct(@RequestParam("page")int page,@RequestParam("size")int size, @RequestParam(name="searchValue", required = false)String input, @RequestParam(name="categoryId", required = false)Long categoryId){
        Pageable pageable = PageRequest.of(page, size);
        return productService.searchProducts(input, categoryId, pageable);
    }

    @GetMapping("/deleteProduct/{id}")
    public @ResponseBody ResponseEntity<?> deleteProduct(@PathVariable Long id){
        Product product = productService.getProductById(id);
        product.setDeleted(true);
        productService.saveProduct(product);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getProductsCount")
    public @ResponseBody Long getProductsCount(){
        return productService.getProductsCount();
    }
    @GetMapping("/products/new")
    public String createProduct(Model model){
        String l = "saveProduct";
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 3);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "product/product_page";
    }

    @PostMapping("/products/saveProduct")
    public @ResponseBody ResponseEntity<?> saveProduct(@Valid @ModelAttribute("product") ProductRequest product, @ValidFile @RequestParam(name = "mainImage", required = false) MultipartFile mainImage,
                                                      @RequestParam("mainImageName") String mainImageName, @RequestParam(name = "adTypes", required = false) Long [] adTypes) throws IOException {
        productService.createAndSaveProduct(product,adTypes,mainImage,mainImageName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model){
        String l = "editProduct";
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 3);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "product/product_page";
    }
    @GetMapping("/products/edit/getProduct/{id}")
    public @ResponseBody ProductResponse getProduct(@PathVariable Long id){
        return productService.getProductResponseById(id);
    }
    @PostMapping("/products/edit/editProduct")
    public @ResponseBody ResponseEntity<?> updateProduct(@Valid @ModelAttribute("product") ProductRequest product, @RequestParam(name = "adTypes", required = false) Long [] adTypes,
                                                         @ValidFile @RequestParam(name = "mainImage", required = false) MultipartFile mainImage, @RequestParam("mainImageName") String mainImageName) throws IOException {
        productService.updateProduct(product,adTypes,mainImage,mainImageName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
