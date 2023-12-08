package project.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.model.adminModel.*;
import project.entity.*;
import project.service.AdminService;
import project.service.CityService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    private final AdminService adminService;
    private final CityService cityService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminController(AdminService adminService, CityService cityService) {
        this.adminService = adminService;
        this.cityService = cityService;
    }

    private int pageSize = 5;
    @GetMapping("/admins")
    public String admins(Model model){
        List<Role> roles = new ArrayList<Role>(Arrays.asList(Role.values()));
        roles.remove(0);
        model.addAttribute("pageNum", 9);
        model.addAttribute("roles", roles);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
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
    @GetMapping("/admins/getCities")
    public @ResponseBody Page<City> getCities(@RequestParam(value = "search", required = false)String name, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1, 2);
        return cityService.getCities(pageable, name);
    }
    @GetMapping("/deleteAdmin/{id}")
    public @ResponseBody ResponseEntity deleteAdmin(@PathVariable Long id){
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/admins/getRoles")
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "admin/admin_page";
    }
    @GetMapping("/admins/edit/getAdmin/{id}")
    public @ResponseBody AdminResponse getAdmin(@PathVariable Long id){
        return adminService.getAdminResponseById(id);
    }
    @PostMapping("/admins/edit/editAdmin")
    public @ResponseBody List<FieldError> updateAdmin(@Valid @ModelAttribute("editAdmin") AdminRequest adminRequest, BindingResult bindingResult){
        Optional<Admin> admin = adminService.getAdminByEmail(adminRequest.getEmail());

        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
            if(admin.isPresent() && admin.get().getId() != adminRequest.getId()){
                FieldError fieldError = new FieldError("Email exist","email","Така пошта вже існує");
                fieldErrors.add(fieldError);
            }
            return fieldErrors;
        }
        if(admin.isPresent() && admin.get().getId() != adminRequest.getId()){
            List<FieldError> fieldErrors = new ArrayList<>(1);
            FieldError fieldError = new FieldError("Email exist","email","Така пошта вже існує");
            fieldErrors.add(fieldError);
            return fieldErrors;
        }
        adminService.updateAdmin(adminRequest);
        return null;
    }
    @GetMapping("/admins/new")
    public String newAdmin(Model model){
        model.addAttribute("pageNum", 9);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "admin/save_admin_page";
    }
    @PostMapping("/admins/saveAdmin")
    public @ResponseBody List<FieldError> saveAdmin(@Valid @ModelAttribute("saveAdmin") SaveAdminRequest adminRequest, BindingResult bindingResult){
        Optional<Admin> admin = adminService.getAdminByEmail(adminRequest.getEmail());
        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
            if(admin.isPresent()){
                FieldError fieldError = new FieldError("Email exist","email","Така пошта вже існує");
                fieldErrors.add(fieldError);
            }
            if(!adminRequest.getNewPassword().equals(adminRequest.getConfirmNewPassword())){
                FieldError fieldError = new FieldError("confirmNewPassword","confirmNewPassword","Невірний пароль");
                fieldErrors.add(fieldError);
            }
            return fieldErrors;
        }
        if(admin.isPresent()){
            List<FieldError> fieldErrors = new ArrayList<>();
            FieldError fieldError = new FieldError("Email exist","email","Така пошта вже існує");
            fieldErrors.add(fieldError);
            if(!adminRequest.getNewPassword().equals(adminRequest.getConfirmNewPassword())){
                FieldError fieldError1 = new FieldError("confirmNewPassword","confirmNewPassword","Невірний пароль");
                fieldErrors.add(fieldError1);
            }
            return fieldErrors;
        }
        if(!adminRequest.getNewPassword().equals(adminRequest.getConfirmNewPassword())){
            List<FieldError> fieldErrors = new ArrayList<>();
            FieldError fieldError = new FieldError("confirmNewPassword","confirmNewPassword","Невірний пароль");
            fieldErrors.add(fieldError);
            return fieldErrors;
        }
        adminService.createAndSaveAdmin(adminRequest);
        return null;
    }



    @GetMapping("/profile")
    public String profile(Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "adminProfile/profile";
    }
    @GetMapping("/getProfile")
    public @ResponseBody ProfileResponse getProfile(@RequestParam("email")String email){
        return adminService.getProfileResponseByEmail(email);
    }
    @PostMapping("/editProfile")
    public @ResponseBody List<FieldError> editProfile(@Valid @ModelAttribute("profile") ProfileDTO profileDTO, BindingResult bindingResult,
                                                      @RequestParam("oldPassword")String oldPassword, @RequestParam("newPassword")String newPassword,
                                                      @RequestParam("confirmNewPassword")String confirmNewPassword, @RequestParam(name = "profileImage", required = false) MultipartFile profileImage) throws IOException {
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
        adminService.updateAdminProfile(profileDTO,newPassword,confirmNewPassword,oldPassword,profileImage);
        return null;
    }

}
