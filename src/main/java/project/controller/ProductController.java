package project.controller;

import jakarta.validation.Valid;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.dto.ProductDTO;
import project.entity.AdditiveType;
import project.entity.Category;
import project.entity.Location;
import project.entity.Product;
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
    @GetMapping("/admin/products")
    public String products(Model model){
        model.addAttribute("pageNum", 3);
        model.addAttribute("categories", categoryService.getCategoriesName());
        return "product/products";
    }
    @GetMapping("/admin/getProducts")
    public @ResponseBody Page<ProductDTO> getProducts(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return productService.getProducts(pageable);
    }
    @GetMapping("/admin/searchProduct")
    public @ResponseBody Page<ProductDTO> searchProduct(@RequestParam("page")int page, @RequestParam(name="searchValue", required = false)String input, @RequestParam(name="categoryId", required = false)Long categoryId){
        Pageable pageable = PageRequest.of(page, pageSize);
        return productService.searchProducts(input, categoryId, pageable);
    }

    @GetMapping("/admin/deleteProduct/{id}")
    public @ResponseBody String deleteProduct(@PathVariable Long id){
        Product product = productService.getProductById(id);
        product.setDeleted(true);
        productService.saveProduct(product);
        return "success";
    }
    @GetMapping("/admin/products/new")
    public String createProduct(Model model){
        String l = "new";
        model.addAttribute("categories", categoryService.getCategoriesName());
        model.addAttribute("product", new Product());
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 3);
        model.addAttribute("additiveTypes", additiveTypeService.getAdditiveTypeNames());
        return "product/product_page";
    }

    @PostMapping("/admin/products/new")
    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
                              @RequestParam("mainImage") MultipartFile mainImage, @RequestParam("mainImageName") String mainImageName,
                              @RequestParam("selectCategory")Long categoryId, @RequestParam(name = "adTypes", required = false) Long [] adTypes,
                              Model model){
        if(isSupportedExtension(FilenameUtils.getExtension(
                mainImage.getOriginalFilename()))) {
            saveImage(mainImage, product, mainImageName);
        } else if(!mainImage.getOriginalFilename().equals("")){
            model.addAttribute("mainWarning", "Некоректний тип файлу");
        }
        if (bindingResult.hasErrors()) {
            String l = "new";
            model.addAttribute("product",product);
            model.addAttribute("link", l);
            model.addAttribute("pageNum", 3);
            model.addAttribute("categories", categoryService.getCategoriesName());
            model.addAttribute("additiveTypes", additiveTypeService.getAdditiveTypeNames());
            return "product/product_page";
        }

        if(!mainImage.getOriginalFilename().equals("") && !isSupportedExtension(FilenameUtils.getExtension(
                mainImage.getOriginalFilename()))){
            String l = "new";
            model.addAttribute("product",product);
            model.addAttribute("link", l);
            model.addAttribute("pageNum", 3);
            model.addAttribute("categories", categoryService.getCategoriesName());
            model.addAttribute("additiveTypes", additiveTypeService.getAdditiveTypeNames());
            return "product/product_page";
        }
        if(adTypes != null){
            List<AdditiveType> additiveTypes = new ArrayList<>();
            for(int i = 0; i < adTypes.length; i++){
                AdditiveType additiveType = additiveTypeService.getAdditiveTypeById(adTypes[i]);
                additiveTypes.add(additiveType);
            }
            product.setAdditiveTypes(additiveTypes);
        }
        Category category = categoryService.getCategoryById(categoryId);
        product.setCategory(category);
        product.setDeleted(false);
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model){
        String l = "edit/"+id;
        Product product = productService.getProductWithAdditiveTypesById(id);
        String chosenAdditiveTypes = "";
        for(AdditiveType at: product.getAdditiveTypes()){
            chosenAdditiveTypes += at.getId();
        }
        model.addAttribute("categories", categoryService.getCategoriesName());
        model.addAttribute("categoryId", product.getCategory().getId());
        model.addAttribute("product", product);
        model.addAttribute("chosenAdditiveTypes", chosenAdditiveTypes);
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 3);
        model.addAttribute("additiveTypes", additiveTypeService.getAdditiveTypeNames());
        return "product/product_page";
    }

    @PostMapping("/admin/products/edit/{id}")
    public String updateProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
                                @RequestParam("selectCategory")Long categoryId, @RequestParam(name = "adTypes", required = false) Long [] adTypes,
                                @RequestParam("mainImage") MultipartFile mainImage, @RequestParam("mainImageName") String mainImageName, Model model) {
        if(isSupportedExtension(FilenameUtils.getExtension(
                mainImage.getOriginalFilename()))) {
            saveImage(mainImage, product, mainImageName);
        } else if(!mainImage.getOriginalFilename().equals("")){
            model.addAttribute("mainWarning", "Некоректний тип файлу");
        }
        if (bindingResult.hasErrors()) {
            String l = "edit/"+product.getId();
            model.addAttribute("product", product);
            model.addAttribute("link", l);
            model.addAttribute("pageNum", 3);
            model.addAttribute("categories", categoryService.getCategoriesName());
            model.addAttribute("additiveTypes", additiveTypeService.getAdditiveTypeNames());
            return "product/product_page";
        }
        if(!mainImage.getOriginalFilename().equals("") && !isSupportedExtension(FilenameUtils.getExtension(
                mainImage.getOriginalFilename()))){
            String l = "edit/"+product.getId();
            System.out.println(product.getImage());
            System.out.println(mainImageName);
            model.addAttribute("product",product);
            model.addAttribute("link", l);
            model.addAttribute("pageNum", 3);
            model.addAttribute("categories", categoryService.getCategoriesName());
            model.addAttribute("additiveTypes", additiveTypeService.getAdditiveTypeNames());
            return "product/product_page";
        }
        Product productInDB = productService.getProductWithAdditiveTypesById(product.getId());
        productInDB.setName(product.getName());
        productInDB.setPrice(product.getPrice());
        productInDB.setStatus(product.getStatus());
        productInDB.setDescription(product.getDescription());
        productInDB.setImage(product.getImage());
        if(adTypes != null) {
            for (int i = 0; i < productInDB.getAdditiveTypes().size(); i++) {
                if (i < adTypes.length && productInDB.getAdditiveTypes().get(i).getId() != adTypes[i]) {
                    if (productInDB.getAdditiveTypes().get(i).getId() < adTypes[i]) {
                        productInDB.getAdditiveTypes().remove(i);
                    }
                }
            }
            if (adTypes.length < productInDB.getAdditiveTypes().size()) {
                for (int l = adTypes.length; l < productInDB.getAdditiveTypes().size(); l++) {
                    productInDB.getAdditiveTypes().remove(l);
                }
            }
            if (adTypes.length > productInDB.getAdditiveTypes().size()) {
                for (int l = productInDB.getAdditiveTypes().size(); l < adTypes.length; l++) {
                    AdditiveType additiveType = additiveTypeService.getAdditiveTypeById(adTypes[l]);
                    productInDB.getAdditiveTypes().add(additiveType);
                }
            }
        }
        System.out.println(productInDB.getAdditiveTypes().size());
        productService.saveProduct(productInDB);
        return "redirect:/admin/products";
    }

    private void saveImage(MultipartFile image, Product product, String name){
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }
        if (!image.getOriginalFilename().equals("") && name.equals("")) {
            String uuidFile = UUID.randomUUID().toString();
            String uniqueName = uuidFile + "." + image.getOriginalFilename();
            product.setImage(uniqueName);
            Path path = Paths.get(uploadPath + "/" + uniqueName);
            try {
                image.transferTo(new File(path.toUri()));
            } catch (IOException e) {
            }
        } else if (image.getOriginalFilename().equals("") && name.equals("")) {
            File file = new File(uploadPath + "/" + product.getImage());
            file.delete();
            product.setImage(null);
        }else if(!image.getOriginalFilename().equals(name)&& !image.getOriginalFilename().equals("")){
            String uuidFile = UUID.randomUUID().toString();
            String uniqueName = uuidFile+"."+image.getOriginalFilename();
            product.setImage(uniqueName);
            Path path = Paths.get(uploadPath+"/"+uniqueName);
            try {
                image.transferTo(new File(path.toUri()));
            } catch (IOException e) {
            }
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
