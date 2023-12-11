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
import project.validators.fileExtentionValidator.ValidFile;

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
    public @ResponseBody ResponseEntity<?> deleteAdmin(@PathVariable Long id){
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
    public @ResponseBody ResponseEntity<?> updateAdmin(@Valid @ModelAttribute("editAdmin") AdminRequest adminRequest){
        adminService.updateAdmin(adminRequest);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public @ResponseBody ResponseEntity<?> saveAdmin(@Valid @ModelAttribute("saveAdmin") SaveAdminRequest adminRequest){
        adminService.createAndSaveAdmin(adminRequest);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public @ResponseBody ResponseEntity<?> editProfile(@Valid @ModelAttribute("profile") ProfileDTO profileDTO, @RequestParam("oldPassword")String oldPassword, @RequestParam("newPassword")String newPassword,
                                                      @RequestParam("confirmNewPassword")String confirmNewPassword, @ValidFile @RequestParam(name = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        adminService.updateAdminProfile(profileDTO,newPassword,confirmNewPassword,oldPassword,profileImage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
