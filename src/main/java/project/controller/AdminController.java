package project.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.model.adminModel.*;
import project.entity.*;
import project.service.AdminService;
import project.service.CityService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AdminController {
    private final AdminService adminService;
    private final CityService cityService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminController(AdminService adminService, CityService cityService) {
        this.adminService = adminService;
        this.cityService = cityService;
    }

    private int pageSize = 1;
    @GetMapping("/admins")
    public String admins(Model model){
        List<Role> roles = new ArrayList<Role>(Arrays.asList(Role.values()));
        roles.remove(0);

        model.addAttribute("pageNum", 9);
        model.addAttribute("roles", roles);
        return "admin/admins";
    }
    @GetMapping("/getAdmins")
    public @ResponseBody Page<AdminDTO> getAdmins(@RequestParam("page")int page, @RequestParam("email")String email){
        Pageable pageable = PageRequest.of(page, pageSize);
        return adminService.getAdmins(pageable, email);
    }
    @GetMapping("/searchAdmins")
    public @ResponseBody Page<AdminDTO> searchAdmins(@RequestParam("page")int page, @RequestParam(value = "searchValue", required = false)String input, @RequestParam(value = "role", required = false)Role role, @RequestParam("email")String email){
        Pageable pageable = PageRequest.of(page, pageSize);
        return adminService.searchAdmins(input,role,email,pageable);
    }
    @GetMapping("/admins/edit/getCities")
    public @ResponseBody Page<City> getCities(@RequestParam(value = "search", required = false)String name, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1, 2);
        return cityService.getCities(pageable, name);
    }
    @GetMapping("/deleteAdmin/{id}")
    public @ResponseBody ResponseEntity deleteAdmin(@PathVariable Long id){
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/admins/edit/getRoles")
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
    @GetMapping("/admins/edit/{id}")
    public String editAdmin(@PathVariable Long id, Model model){
        model.addAttribute("pageNum", 9);
        return "admin/admin_page";
    }
    @GetMapping("/admins/edit/getAdmin/{id}")
    public @ResponseBody AdminResponse getAdmin(@PathVariable Long id){
        return adminService.getAdminResponseById(id);
    }
    @PostMapping("/admins/edit/editAdmin")
    public @ResponseBody List<FieldError> updateAdmin(@Valid @ModelAttribute("editAdmin") AdminRequest admin, BindingResult bindingResult){
        Admin admin1 = adminService.getAdminByEmail(admin.getEmail());
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
        adminService.updateAdmin(admin);
        return null;
    }
    @GetMapping("/profile")
    public String profile(){
        return "adminProfile/profile";
    }
    @GetMapping("/getProfile")
    public @ResponseBody ProfileDTO getProfile(@RequestParam("email")String email){
        return adminService.getProfileResponseByEmail(email);
    }
    @PostMapping("/editProfile")
    public @ResponseBody List<FieldError> editProfile(@Valid @ModelAttribute("profile") ProfileDTO profileDTO, BindingResult bindingResult, @RequestParam("oldPassword")String oldPassword, @RequestParam("newPassword")String newPassword, @RequestParam("confirmNewPassword")String confirmNewPassword){
        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
            if(!oldPassword.equals("") && !newPassword.equals("") && !confirmNewPassword.equals("")){
                Admin admin = adminService.getAdminById(profileDTO.getId());
                if(!bCryptPasswordEncoder.matches(oldPassword, admin.getPassword())){
                    FieldError fieldError = new FieldError("Old password wrong","oldPassword","Невірний пароль");
                    fieldErrors.add(fieldError);
                }
            }
            if(confirmNewPassword.equals("")){
                if(!newPassword.equals("")) {
                    FieldError fieldError = new FieldError("Confirm password blank", "confirmNewPassword", "Поле не може бути порожнім");
                    fieldErrors.add(fieldError);
                } else if(!oldPassword.equals("")){
                    FieldError fieldError = new FieldError("Confirm password blank", "confirmNewPassword", "Поле не може бути порожнім");
                    fieldErrors.add(fieldError);
                }
            }
            if(newPassword.equals("")){
                if(!confirmNewPassword.equals("")) {
                    FieldError fieldError = new FieldError("New password blank", "newPassword", "Поле не може бути порожнім");
                    fieldErrors.add(fieldError);
                } else if(!oldPassword.equals("")){
                    FieldError fieldError = new FieldError("New password blank", "newPassword", "Поле не може бути порожнім");
                    fieldErrors.add(fieldError);
                }
            }
            if(oldPassword.equals("")){
                if(!confirmNewPassword.equals("")) {
                    FieldError fieldError = new FieldError("Old password blank", "oldPassword", "Поле не може бути порожнім");
                    fieldErrors.add(fieldError);
                } else if(!newPassword.equals("")){
                    FieldError fieldError = new FieldError("Old password blank", "oldPassword", "Поле не може бути порожнім");
                    fieldErrors.add(fieldError);
                }
            }
            if(!newPassword.equals("") && !confirmNewPassword.equals("") && !newPassword.equals(confirmNewPassword)){
                FieldError fieldError = new FieldError("Confirm password wrong","confirmNewPassword","Невірний пароль");
                fieldErrors.add(fieldError);
            }
            return fieldErrors;
        }
        List<FieldError> fieldErrors = new ArrayList<>();
        if(!oldPassword.equals("") && !newPassword.equals("") && !confirmNewPassword.equals("")){
            Admin admin = adminService.getAdminById(profileDTO.getId());
            if(!bCryptPasswordEncoder.matches(oldPassword, admin.getPassword())){
                FieldError fieldError = new FieldError("Old password wrong","oldPassword","Невірний пароль");
                fieldErrors.add(fieldError);
            }
        }
        if(confirmNewPassword.equals("")){
            if(!newPassword.equals("")) {
                FieldError fieldError = new FieldError("Confirm password blank", "confirmNewPassword", "Поле не може бути порожнім");
                fieldErrors.add(fieldError);
            } else if(!oldPassword.equals("")){
                FieldError fieldError = new FieldError("Confirm password blank", "confirmNewPassword", "Поле не може бути порожнім");
                fieldErrors.add(fieldError);
            }
        }
        if(newPassword.equals("")){
            if(!confirmNewPassword.equals("")) {
                FieldError fieldError = new FieldError("New password blank", "newPassword", "Поле не може бути порожнім");
                fieldErrors.add(fieldError);
            } else if(!oldPassword.equals("")){
                FieldError fieldError = new FieldError("New password blank", "newPassword", "Поле не може бути порожнім");
                fieldErrors.add(fieldError);
            }
        }
        if(oldPassword.equals("")){
            if(!confirmNewPassword.equals("")) {
                FieldError fieldError = new FieldError("Old password blank", "oldPassword", "Поле не може бути порожнім");
                fieldErrors.add(fieldError);
            } else if(!newPassword.equals("")){
                FieldError fieldError = new FieldError("Old password blank", "oldPassword", "Поле не може бути порожнім");
                fieldErrors.add(fieldError);
            }
        }
        if(!newPassword.equals("") && !confirmNewPassword.equals("") && !newPassword.equals(confirmNewPassword)){
            FieldError fieldError = new FieldError("Confirm password wrong","confirmNewPassword","Невірний пароль");
            fieldErrors.add(fieldError);
        }
        if(fieldErrors.size() != 0){
            return fieldErrors;
        }
        adminService.updateAdminProfile(profileDTO,newPassword,confirmNewPassword,oldPassword);
        return null;
    }

}
