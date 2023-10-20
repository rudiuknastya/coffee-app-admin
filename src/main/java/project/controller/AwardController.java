package project.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.model.AwardDTO;
import project.model.productModel.ProductNameDTO;
import project.entity.Product;
import project.entity.User;
import project.service.AwardService;
import project.service.ProductService;
import project.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AwardController {
    private final AwardService awardService;
    private final ProductService productService;
    private final UserService userService;

    public AwardController(AwardService awardService, ProductService productService, UserService userService) {
        this.awardService = awardService;
        this.productService = productService;
        this.userService = userService;
    }

    private int pageSize = 1;
    @GetMapping("/admin/awards")
    public String showAwards(Model model){
        model.addAttribute("pageNum", 11);
        //model.addAttribute("products", productService.getProductNames());
        return "award/awards";
    }
    @GetMapping("/admin/getAwards")
    public @ResponseBody Page<AwardDTO> getAwards(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return awardService.getAwards(pageable);
    }
    @GetMapping("/admin/searchAwards")
    public @ResponseBody Page<AwardDTO> searchAwards(@RequestParam("page")int page, @RequestParam("phone")String phone){
        Pageable pageable = PageRequest.of(page, pageSize);
        return awardService.searchAwards(phone, pageable);
    }
    @GetMapping("/admin/deleteAward")
    public @ResponseBody String deleteAward(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId){
        User user = userService.getUserWithProducts(userId);
        int count = 0;
        System.out.println(user.getProducts());
        int productToRemove = 0;
        int i = 0;
        for(Product product: user.getProducts()){
            if(product.getId() == productId && count <= 0){
                productToRemove = i;
                count++;
            }
            i++;
        }
        user.getProducts().remove(productToRemove);
        userService.saveUser(user);
        return "success";
    }

    @GetMapping("/admin/getProductForAward/{id}")
    public @ResponseBody ProductNameDTO editAward(@PathVariable Long id){
        return productService.getProductNameDTO(id);
    }
    @GetMapping("/admin/getProductsForAward")
    public @ResponseBody Page<ProductNameDTO> getProductsForAward(@RequestParam(value = "search", required = false)String name, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1, pageSize);
        return productService.getProductNameDTOS(pageable, name);
    }
    @PostMapping("/admin/editAward")
    public @ResponseBody String updateAward(@RequestParam("userId") Long userId, @RequestParam("newProductId") Long newProductId, @RequestParam("oldProductId") Long oldProductId){
        User user = userService.getUserWithProducts(userId);
        int i = 0;
        for(Product product: user.getProducts()){
            if(product.getId() == oldProductId){
                Product product1 = productService.getProductById(newProductId);
                user.getProducts().set(i, product1);
            }
            i++;
        }
        userService.saveUser(user);
        return "success";
    }
}
