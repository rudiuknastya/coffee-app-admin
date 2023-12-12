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
import project.model.additiveModel.AdditiveDTO;
import project.model.additiveModel.AdditiveRequest;
import project.model.additiveTypeModel.AdditiveTypeNameDTO;
import project.entity.Additive;
import project.entity.AdditiveType;
import project.service.AdditiveService;
import project.service.AdditiveTypeService;
import project.service.AdminService;

import java.util.List;
import java.util.Optional;

@Controller
public class AdditiveController {
    private final AdditiveService additiveService;
    private final AdditiveTypeService additiveTypeService;
    private final AdminService adminService;

    public AdditiveController(AdditiveService additiveService, AdditiveTypeService additiveTypeService, AdminService adminService) {
        this.additiveService = additiveService;
        this.additiveTypeService = additiveTypeService;
        this.adminService = adminService;
    }

    private int pageSize = 5;
    @GetMapping("/additives")
    public String additives(Model model){
        model.addAttribute("pageNum", 5);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "additive/additives";
    }

    @GetMapping("/getAdditives")
    public @ResponseBody Page<AdditiveDTO> getAdditives(@RequestParam("page")int page, @RequestParam("size")int size){
        Pageable pageable = PageRequest.of(page, size);
        return additiveService.getAllAdditives(pageable);
    }
    @GetMapping("/getAdTypesForAdditives")
    public @ResponseBody Page<AdditiveTypeNameDTO> getAdTypesForAdditives(@RequestParam(value = "search", required = false)String name,@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1, pageSize);
        return additiveTypeService.getAdditiveTypeNames(pageable, name);
    }
    @GetMapping("/getAdTypeForAdditive/{id}")
    public @ResponseBody AdditiveTypeNameDTO getAdTypeForAdditive(@PathVariable Long id){
        return additiveTypeService.getAdditiveTypeNameDTOById(id);
    }
    @GetMapping("/deleteAdditive/{id}")
    public @ResponseBody ResponseEntity deleteAdditive(@PathVariable Long id){
        Additive additive = additiveService.getAdditiveById(id);
        additive.setDeleted(true);
        additiveService.saveAdditive(additive);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/saveAdditive")
    public @ResponseBody ResponseEntity<?> saveAdditive(@Valid @ModelAttribute("saveAdditive") AdditiveRequest additive){
        additiveService.createAdditive(additive);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/editAdditive/{id}")
    public @ResponseBody Additive editAdditive(@PathVariable Long id){
        return additiveService.getAdditiveById(id);
    }
    @PostMapping("/editAdditive")
    public @ResponseBody ResponseEntity<?> updateAdditive(@Valid @ModelAttribute("editAdditive") AdditiveRequest additive){
        additiveService.updateAdditive(additive);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/searchAdditive")
    public @ResponseBody Page<AdditiveDTO> searchAdditive(@RequestParam("page")int page,@RequestParam("size")int size, @RequestParam(name="searchValue", required = false) String input, @RequestParam(name="additiveType", required = false) Long additiveType){
        Pageable pageable = PageRequest.of(page,size);
        return additiveService.searchAdditive(input, additiveType, pageable);
    }
}
