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
import project.model.additiveTypeModel.AdditiveTypeDTO;
import project.entity.Additive;
import project.entity.AdditiveType;
import project.model.additiveTypeModel.AdditiveTypeRequest;
import project.service.AdditiveService;
import project.service.AdditiveTypeService;
import project.service.AdminService;

import java.util.List;
import java.util.Optional;

@Controller
public class AdditiveTypeController {
    private final AdditiveTypeService additiveTypeService;
    private final AdminService adminService;


    public AdditiveTypeController(AdditiveTypeService additiveTypeService, AdminService adminService) {
        this.additiveTypeService = additiveTypeService;
        this.adminService = adminService;
    }

    private int pageSize = 5;

    @GetMapping("/additiveTypes")
    public String additiveTypes(Model model){
        model.addAttribute("pageNum", 4);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "additiveType/additive_types";
    }

    @GetMapping("/getAdditiveTypes")
    public @ResponseBody Page<AdditiveTypeDTO> getAdditiveTypes(@RequestParam("page")int page,@RequestParam("size")int size){
        Pageable pageable = PageRequest.of(page,size);
        return additiveTypeService.getAdditiveTypes(pageable);
    }

    @GetMapping("/deleteAdditiveType/{id}")
    public @ResponseBody ResponseEntity<?> deleteAdditiveType(@PathVariable Long id){
        additiveTypeService.deleteAdditiveType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/saveAdditiveType")
    public @ResponseBody ResponseEntity<?> saveAdditiveType(@Valid @ModelAttribute("saveAdditiveType") AdditiveTypeRequest additiveTypeRequest){
        additiveTypeService.createAndSaveAdditiveType(additiveTypeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/editAdditiveType/{id}")
    public @ResponseBody AdditiveType editAdditiveType(@PathVariable Long id){
        return additiveTypeService.getAdditiveTypeById(id);
    }
    @PostMapping("/editAdditiveType")
    public @ResponseBody ResponseEntity<?> updateAdditiveType(@Valid @ModelAttribute("editAdditiveType") AdditiveTypeRequest additiveTypeRequest){
        additiveTypeService.updateAdditiveType(additiveTypeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/searchAdditiveTypes")
    public @ResponseBody Page<AdditiveTypeDTO> searchAdditiveTypes(@RequestParam("page")int page,@RequestParam("size")int size, @RequestParam("name")String name){
        Pageable pageable = PageRequest.of(page,size);
        return additiveTypeService.searchAdditiveTypes(name, pageable);
    }
}
