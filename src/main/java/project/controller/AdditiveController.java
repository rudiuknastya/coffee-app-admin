package project.controller;

import jakarta.validation.Valid;
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
import project.model.additiveModel.AdditiveDTO;
import project.model.additiveModel.AdditiveRequest;
import project.model.additiveTypeModel.AdditiveTypeNameDTO;
import project.entity.Additive;
import project.entity.AdditiveType;
import project.service.AdditiveService;
import project.service.AdditiveTypeService;

import java.util.List;

@Controller
public class AdditiveController {
    private final AdditiveService additiveService;
    private final AdditiveTypeService additiveTypeService;

    public AdditiveController(AdditiveService additiveService, AdditiveTypeService additiveTypeService) {
        this.additiveService = additiveService;
        this.additiveTypeService = additiveTypeService;
    }

    private int pageSize = 2;
    @GetMapping("/additives")
    public String additives(Model model){
        model.addAttribute("pageNum", 5);
        return "additive/additives";
    }

    @GetMapping("/getAdditives")
    public @ResponseBody Page<AdditiveDTO> getAdditives(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
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
    public @ResponseBody List<FieldError> saveAdditive(@Valid @ModelAttribute("saveAdditive") AdditiveRequest additive, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return bindingResult.getFieldErrors();
        }
        additiveService.createAdditive(additive);
        return null;
    }
    @GetMapping("/editAdditive/{id}")
    public @ResponseBody Additive editAdditive(@PathVariable Long id){
        return additiveService.getAdditiveById(id);
    }
    @PostMapping("/editAdditive")
    public @ResponseBody List<FieldError> updateAdditive(@Valid @ModelAttribute("editAdditive") AdditiveRequest additive, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return bindingResult.getFieldErrors();
        }
        additiveService.updateAdditive(additive);
        return null;
    }
    @GetMapping("/searchAdditive")
    public @ResponseBody Page<AdditiveDTO> searchAdditive(@RequestParam("page")int page, @RequestParam(name="searchValue", required = false) String input, @RequestParam(name="additiveType", required = false) Long additiveType){
        Pageable pageable = PageRequest.of(page,pageSize);
        return additiveService.searchAdditive(input, additiveType, pageable);
    }
}
