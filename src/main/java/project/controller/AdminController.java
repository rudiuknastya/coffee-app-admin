package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.model.AdminDTO;
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
    @GetMapping("/admin/admins/edit/getCity")
    public @ResponseBody City getCity( @RequestParam("city")String city){
        return cityService.getCityByName(city);
    }
    @GetMapping("/admin/deleteAdmin/{id}")
    public @ResponseBody String deleteAdmin(@PathVariable Long id){
        adminService.deleteAdmin(id);
        return "success";
    }
    @GetMapping("/admin/admins/edit/{id}")
    public String editAdmin(@PathVariable Long id, Model model){
        String l = "edit/"+id;
        List<Role> roles = new ArrayList<Role>(Arrays.asList(Role.values()));
        roles.remove(0);
        model.addAttribute("admin", adminService.getAdminById(id));
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 9);
        model.addAttribute("roles", roles);
        //model.addAttribute("cities",cityService.getAllCities());
        return "admin/admin_page";
    }
    @PostMapping("/admin/admins/edit/{id}")
    public String updateAdmin(@Valid @ModelAttribute("admin") Admin admin, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()) {
            String l = "edit/" + admin.getId();
            List<Role> roles = new ArrayList<Role>(Arrays.asList(Role.values()));
            roles.remove(0);
            model.addAttribute("link", l);
            model.addAttribute("pageNum", 9);
            model.addAttribute("roles", roles);
//            model.addAttribute("cities", cityService.getAllCities());
            return "admin/admin_page";
        }
        Admin adminInDB = adminService.getAdminById(admin.getId());
        adminInDB.setFirstName(admin.getFirstName());
        adminInDB.setLastName(admin.getLastName());
        adminInDB.setEmail(admin.getEmail());
        adminInDB.setCity(admin.getCity());
        adminInDB.setBirthDate(admin.getBirthDate());
        adminInDB.setRole(admin.getRole());
        adminService.saveAdmin(adminInDB);
        return "redirect:/admin/admins";
    }
}
