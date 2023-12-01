package project.controller;

import jakarta.validation.Valid;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.multipart.MultipartFile;
import project.model.CategoryNameDTO;
import project.model.productModel.ProductDTO;
import project.entity.AdditiveType;
import project.entity.Product;
import project.model.productModel.ProductRequest;
import project.model.productModel.ProductResponse;
import project.service.AdditiveTypeService;
import project.service.CategoryService;
import project.service.ProductService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final AdditiveTypeService additiveTypeService;

    public ProductController(ProductService productService, CategoryService categoryService, AdditiveTypeService additiveTypeService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.additiveTypeService = additiveTypeService;
    }
    String uploadPath = "C:\\Users\\Anastassia\\IdeaProjects\\Coffee-app-admin\\uploads";
    private int pageSize = 1;
    @GetMapping("/products")
    public String products(Model model){
        model.addAttribute("pageNum", 3);
        return "product/products";
    }
    @GetMapping("/getProducts")
    public @ResponseBody Page<ProductDTO> getProducts(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
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
    public @ResponseBody Page<ProductDTO> searchProduct(@RequestParam("page")int page, @RequestParam(name="searchValue", required = false)String input, @RequestParam(name="categoryId", required = false)Long categoryId){
        Pageable pageable = PageRequest.of(page, pageSize);
        return productService.searchProducts(input, categoryId, pageable);
    }

    @GetMapping("/deleteProduct/{id}")
    public @ResponseBody ResponseEntity deleteProduct(@PathVariable Long id){
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
        return "product/product_page";
    }

    @PostMapping("/products/saveProduct")
    public @ResponseBody List<FieldError> saveProduct(@Valid @ModelAttribute("product") ProductRequest product, BindingResult bindingResult,
                                                      @RequestParam(name = "mainImage", required = false) MultipartFile mainImage, @RequestParam("mainImageName") String mainImageName,
                                                      @RequestParam(name = "adTypes", required = false) Long [] adTypes) throws IOException {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
            if(mainImage != null && !mainImage.getOriginalFilename().equals("") && !isSupportedExtension(FilenameUtils.getExtension(
                    mainImage.getOriginalFilename()))) {
                FieldError fieldError = new FieldError("Image format wrong","image","Некоректний тип файлу");
                fieldErrors.add(fieldError);
            }
            return fieldErrors;
        }

        if(mainImage != null && !mainImage.getOriginalFilename().equals("") && !isSupportedExtension(FilenameUtils.getExtension(
                mainImage.getOriginalFilename()))){
            List<FieldError> fieldErrors = new ArrayList<>(1);
            FieldError fieldError = new FieldError("Image format wrong","image","Некоректний тип файлу");
            fieldErrors.add(fieldError);
            return fieldErrors;
        }
        List<AdditiveType> additiveTypes = null;
        if(adTypes != null){
            additiveTypes = new ArrayList<>();
            for(int i = 0; i < adTypes.length; i++){
                AdditiveType additiveType = additiveTypeService.getAdditiveTypeById(adTypes[i]);
                additiveTypes.add(additiveType);
            }
        }
        Product productToSave = new Product();
        if(mainImage != null) {
            saveImage(mainImage, productToSave, mainImageName);
        }
        productToSave.setName(product.getName());
        productToSave.setDescription(product.getDescription());
        productToSave.setPrice(product.getPrice());
        productToSave.setStatus(product.getStatus());
        productToSave.setDeleted(false);
        productToSave.setCategory(categoryService.getCategoryById(product.getCategoryId()));
        productToSave.setAdditiveTypes(additiveTypes);
        productService.saveProduct(productToSave);
        return null;
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model){
        String l = "editProduct";
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 3);
        return "product/product_page";
    }
    @GetMapping("/products/edit/getProduct/{id}")
    public @ResponseBody ProductResponse getProduct(@PathVariable Long id){
        return productService.getProductResponseById(id);
    }
    @PostMapping("/products/edit/editProduct")
    public @ResponseBody List<FieldError> updateProduct(@Valid @ModelAttribute("product") ProductRequest product,BindingResult bindingResult,
                                @RequestParam(name = "adTypes", required = false) Long [] adTypes,
                                @RequestParam(name = "mainImage", required = false) MultipartFile mainImage, @RequestParam("mainImageName") String mainImageName) throws IOException {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
            if(mainImage != null && !mainImage.getOriginalFilename().equals("") && !isSupportedExtension(FilenameUtils.getExtension(
                    mainImage.getOriginalFilename()))) {
                FieldError fieldError = new FieldError("Image format wrong","image","Некоректний тип файлу");
                fieldErrors.add(fieldError);
            }
            return fieldErrors;
        }

        if(mainImage != null && !mainImage.getOriginalFilename().equals("") && !isSupportedExtension(FilenameUtils.getExtension(
                mainImage.getOriginalFilename()))){
            List<FieldError> fieldErrors = new ArrayList<>(1);
            FieldError fieldError = new FieldError("Image format wrong","image","Некоректний тип файлу");
            fieldErrors.add(fieldError);
            return fieldErrors;
        }
        Product productInDB = productService.getProductWithAdditiveTypesById(product.getId());
        productInDB.getAdditiveTypes().clear();
        productService.saveProduct(productInDB);
        if(adTypes.length > 0) {
            List<AdditiveType> additiveTypes = additiveTypeService.getAdditiveTypesByIds(adTypes);
            productInDB.setAdditiveTypes(additiveTypes);
        }
        if(mainImage != null) {
            saveImage(mainImage, productInDB, mainImageName);
        }
        productInDB.setName(product.getName());
        productInDB.setPrice(product.getPrice());
        productInDB.setStatus(product.getStatus());
        productInDB.setDescription(product.getDescription());
        productInDB.setCategory(categoryService.getCategoryById(product.getCategoryId()));
        productService.saveProduct(productInDB);
        return null;
    }

    private void saveImage(MultipartFile image, Product product, String name) throws IOException {
        if (!image.getOriginalFilename().equals("") && name.equals("")) {
            String uuidFile = UUID.randomUUID().toString();
            String uniqueName = uuidFile + "." + image.getOriginalFilename();
            product.setImage(uniqueName);
            Path path = Paths.get(uploadPath + "/" + uniqueName);
                image.transferTo(new File(path.toUri()));

        } else if (image.getOriginalFilename().equals("") && name.equals("")) {
            File file = new File(uploadPath + "/" + product.getImage());
            file.delete();
            product.setImage(null);
        }else if(!image.getOriginalFilename().equals(name)&& !image.getOriginalFilename().equals("")){
            String uuidFile = UUID.randomUUID().toString();
            String uniqueName = uuidFile+"."+image.getOriginalFilename();
            product.setImage(uniqueName);
            Path path = Paths.get(uploadPath+"/"+uniqueName);
            image.transferTo(new File(path.toUri()));
            File file = new File(uploadPath+"/"+name);
            file.delete();
        } else if (!name.equals("")){
            product.setImage(name);
        }
    }

    private boolean isSupportedExtension(String extension) {
        return extension != null && (
                extension.equals("png")
                        || extension.equals("jpg")
                        || extension.equals("jpeg"));
    }
}
