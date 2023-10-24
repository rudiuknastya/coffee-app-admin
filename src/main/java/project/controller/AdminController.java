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
import project.model.adminModel.AdminDTO;
import project.entity.*;
import project.model.adminModel.AdminRequest;
import project.model.adminModel.AdminResponse;
import project.model.adminModel.RoleDTO;
import project.service.AdminService;
import project.service.CityService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AdminController {
    private final AdminService adminService;
    private final CityService cityService;

    public AdminController(AdminService adminService, CityService cityService) {
        this.adminService = adminService;
        this.cityService = cityService;
    }

    private int pageSize = 1;
    @GetMapping("/admin/admins")
    public String admins(Model model){
        List<Role> roles = new ArrayList<Role>(Arrays.asList(Role.values()));
        roles.remove(0);

        model.addAttribute("pageNum", 9);
        model.addAttribute("roles", roles);
        return "admin/admins";
    }
    @GetMapping("/admin/getAdmins")
    public @ResponseBody Page<AdminDTO> getAdmins(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return adminService.getAdmins(pageable);
    }
    @GetMapping("/admin/searchAdmins")
    public @ResponseBody Page<AdminDTO> searchAdmins(@RequestParam("page")int page, @RequestParam(value = "searchValue", required = false)String input, @RequestParam(value = "role", required = false)Role role){
        Pageable pageable = PageRequest.of(page, pageSize);
        return adminService.searchAdmins(input,role,pageable);
    }
    @GetMapping("/admin/admins/edit/getCities")
    public @ResponseBody Page<City> getCities(@RequestParam(value = "search", required = false)String name, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1, 2);
        return cityService.getCities(pageable, name);
    }
    @GetMapping("/admin/deleteAdmin/{id}")
    public @ResponseBody String deleteAdmin(@PathVariable Long id){
        adminService.deleteAdmin(id);
        return "success";
    }
    @GetMapping("/admin/admins/edit/getRoles")
    public @ResponseBody RoleDTO[] getRoles(){
        Role[] roles = Role.values();
        RoleDTO[] roleDTOS = new RoleDTO[roles.length-1];
        for(int i = 1; i < roles.length; i++){
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRole(roles[i]);
            roleDTO.setName(roles[i].getRoleName());
            roleDTOS[i-1] = roleDTO;
        }
        return roleDTOS;
    }
    @GetMapping("/admin/admins/edit/{id}")
    public String editAdmin(@PathVariable Long id, Model model){
        model.addAttribute("pageNum", 9);
        return "admin/admin_page";
    }
    @GetMapping("/admin/admins/edit/getAdmin/{id}")
    public @ResponseBody AdminResponse getAdmin(@PathVariable Long id){
        return adminService.getAdminResponseById(id);
    }
    @PostMapping("/admin/admins/edit/editAdmin")
    public @ResponseBody List<FieldError> updateAdmin(@Valid @ModelAttribute("editAdmin") AdminRequest admin, BindingResult bindingResult){
        Admin admin1 = adminService.getAdminByEmail(admin.getEmail());
        System.out.println(admin.getBirthDate());
        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
            if(admin1 != null && admin1.getId() != admin.getId()){
                FieldError fieldError = new FieldError("Email exist","email","Така пошта вже існує");
                fieldErrors.add(fieldError);
            }
            return fieldErrors;
        }
        if(admin1 != null && admin1.getId() != admin.getId()){
            List<FieldError> fieldErrors = new ArrayList<>(1);
            FieldError fieldError = new FieldError("Email exist","email","Така пошта вже існує");
            fieldErrors.add(fieldError);
            return fieldErrors;
        }
        Admin adminInDB = adminService.getAdminById(admin.getId());
        adminInDB.setFirstName(admin.getFirstName());
        adminInDB.setLastName(admin.getLastName());
        adminInDB.setEmail(admin.getEmail());
        adminInDB.setCity(admin.getCity());
        adminInDB.setBirthDate(admin.getBirthDate());
        adminInDB.setRole(admin.getRole());
        adminService.saveAdmin(adminInDB);
        return null;
    }
}
