package project.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public AwardController(AwardService awardService, ProductService productService) {
        this.awardService = awardService;
        this.productService = productService;
    }

    private int pageSize = 1;
    @GetMapping("/awards")
    public String showAwards(Model model){
        model.addAttribute("pageNum", 11);
        return "award/awards";
    }
    @GetMapping("/getAwards")
    public @ResponseBody Page<AwardDTO> getAwards(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return awardService.getAwards(pageable);
    }
    @GetMapping("/searchAwards")
    public @ResponseBody Page<AwardDTO> searchAwards(@RequestParam("page")int page, @RequestParam("phone")String phone){
        Pageable pageable = PageRequest.of(page, pageSize);
        return awardService.searchAwards(phone, pageable);
    }
    @GetMapping("/deleteAward")
    public @ResponseBody ResponseEntity deleteAward(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId){
        awardService.deleteAward(userId,productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getProductForAward/{id}")
    public @ResponseBody ProductNameDTO editAward(@PathVariable Long id){
        return productService.getProductNameDTO(id);
    }
    @GetMapping("/getProductsForAward")
    public @ResponseBody Page<ProductNameDTO> getProductsForAward(@RequestParam(value = "search", required = false)String name, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1, pageSize);
        return productService.getProductNameDTOS(pageable, name);
    }
    @PostMapping("/editAward")
    public @ResponseBody ResponseEntity updateAward(@RequestParam("userId") Long userId, @RequestParam("newProductId") Long newProductId, @RequestParam("oldProductId") Long oldProductId){
        awardService.updateAward(userId,newProductId,oldProductId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
