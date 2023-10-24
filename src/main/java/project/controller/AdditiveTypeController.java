package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.model.additiveTypeModel.AdditiveTypeDTO;
import project.entity.Additive;
import project.entity.AdditiveType;
import project.service.AdditiveService;
import project.service.AdditiveTypeService;

import java.util.List;

@Controller
public class AdditiveTypeController {
    private final AdditiveTypeService additiveTypeService;
    private final AdditiveService additiveService;

    public AdditiveTypeController(AdditiveTypeService additiveTypeService, AdditiveService additiveService) {
        this.additiveTypeService = additiveTypeService;
        this.additiveService = additiveService;
    }

    private int pageSize = 1;

    @GetMapping("/admin/additive_types")
    public String additiveTypes(Model model){
        model.addAttribute("pageNum", 4);
        return "additiveType/additive_types";
    }

    @GetMapping("/admin/getAdditiveTypes")
    public @ResponseBody Page<AdditiveTypeDTO> getAdditiveTypes(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page,pageSize);
        return additiveTypeService.getAdditiveTypes(pageable);
    }

    @GetMapping("/admin/deleteAdditiveType/{id}")
    public @ResponseBody String deleteAdditiveType(@PathVariable Long id){
        AdditiveType additiveType = additiveTypeService.getAdditiveTypeById(id);
        additiveType.setDeleted(true);
        List<Additive> additives = additiveService.getAdditivesForAdditiveType(id);
        for(Additive additive: additives){
            additive.setDeleted(true);
            additiveService.saveAdditive(additive);
        }
        additiveTypeService.saveAdditiveType(additiveType);
        return "deleted";
    }
    @PostMapping("/admin/saveAdditiveType")
    public @ResponseBody List<FieldError> saveAdditiveType(@Valid @ModelAttribute("saveAdditiveType") AdditiveType additiveType, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return bindingResult.getFieldErrors();
        }
        additiveType.setDeleted(false);
        additiveTypeService.saveAdditiveType(additiveType);
        return null;
    }
    @GetMapping("/admin/editAdditiveType/{id}")
    public @ResponseBody AdditiveType editAdditiveType(@PathVariable Long id){
        return additiveTypeService.getAdditiveTypeById(id);
    }
    @PostMapping("/admin/editAdditiveType")
    public @ResponseBody List<FieldError> updateAdditiveType(@Valid @ModelAttribute("editAdditiveType") AdditiveType additiveType, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return bindingResult.getFieldErrors();
        }
        AdditiveType additiveTypeInDb = additiveTypeService.getAdditiveTypeById(additiveType.getId());
        additiveTypeInDb.setName(additiveType.getName());
        additiveTypeInDb.setStatus(additiveType.getStatus());
        additiveTypeService.saveAdditiveType(additiveTypeInDb);
        return null;
    }

    @GetMapping("/admin/searchAdditiveTypes")
    public @ResponseBody Page<AdditiveTypeDTO> searchAdditiveTypes(@RequestParam("page")int page, @RequestParam("name")String name){
        Pageable pageable = PageRequest.of(page,1);
        return additiveTypeService.searchAdditiveTypes(name, pageable);
    }
}
