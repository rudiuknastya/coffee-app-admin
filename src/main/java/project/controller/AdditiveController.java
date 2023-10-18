package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.model.additiveModel.AdditiveDTO;
import project.model.additiveModel.AdditiveRequest;
import project.model.additiveTypeModel.AdditiveTypeNameDTO;
import project.entity.Additive;
import project.entity.AdditiveType;
import project.service.AdditiveService;
import project.service.AdditiveTypeService;

@Controller
public class AdditiveController {
    private final AdditiveService additiveService;
    private final AdditiveTypeService additiveTypeService;

    public AdditiveController(AdditiveService additiveService, AdditiveTypeService additiveTypeService) {
        this.additiveService = additiveService;
        this.additiveTypeService = additiveTypeService;
    }

    private int pageSize = 2;
    @GetMapping("/admin/additives")
    public String additives(Model model){
        model.addAttribute("pageNum", 5);
        return "additive/additives";
    }

    @GetMapping("/admin/getAdditives")
    public @ResponseBody Page<AdditiveDTO> getAdditives(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return additiveService.getAllAdditives(pageable);
    }
    @GetMapping("/admin/getAdTypesForAdditives")
    public @ResponseBody Page<AdditiveTypeNameDTO> getAdTypesForAdditives(@RequestParam(value = "search", required = false)String name,@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1, pageSize);
        return additiveTypeService.getAdditiveTypeNames(pageable, name);
    }
    @GetMapping("/admin/getAdTypeForAdditive/{id}")
    public @ResponseBody AdditiveTypeNameDTO getAdTypeForAdditive(@PathVariable Long id){
        return additiveTypeService.getAdditiveTypeNameDTOById(id);
    }
    @GetMapping("/admin/deleteAdditive/{id}")
    public @ResponseBody String deleteAdditive(@PathVariable Long id){
        Additive additive = additiveService.getAdditiveById(id);
        additive.setDeleted(true);
        additiveService.saveAdditive(additive);
        return "deleted";
    }

    @PostMapping("/admin/saveAdditive")
    public @ResponseBody String saveAdditive(@Valid @ModelAttribute("saveAdditive") Additive additive, @RequestParam("selectAdType") Long adTypeId){
        AdditiveType additiveType = additiveTypeService.getAdditiveTypeById(adTypeId);
        additive.setAdditiveType(additiveType);
        additive.setDeleted(false);
        additiveService.saveAdditive(additive);
        return "success";
    }
    @GetMapping("/admin/editAdditive/{id}")
    public @ResponseBody AdditiveRequest editAdditive(@PathVariable Long id){
        return additiveService.getAdditiveRequestById(id);
    }
    @PostMapping("/admin/editAdditive")
    public @ResponseBody String updateAdditive(@Valid @ModelAttribute("editAdditive") Additive additive, @RequestParam("editSelectAdType") Long adTypeId){
        Additive additiveInDB = additiveService.getAdditiveById(additive.getId());
        additiveInDB.setName(additive.getName());
        additiveInDB.setPrice(additive.getPrice());
        additiveInDB.setStatus(additive.getStatus());
        AdditiveType additiveType = additiveTypeService.getAdditiveTypeById(adTypeId);
        additiveInDB.setAdditiveType(additiveType);
        additiveService.saveAdditive(additiveInDB);
        return "success";
    }
    @GetMapping("/admin/searchAdditive")
    public @ResponseBody Page<AdditiveDTO> searchAdditive(@RequestParam("page")int page, @RequestParam(name="searchValue", required = false) String input, @RequestParam(name="additiveType", required = false) Long additiveType){
        Pageable pageable = PageRequest.of(page,pageSize);
        return additiveService.searchAdditive(input, additiveType, pageable);
    }
}
