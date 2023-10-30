package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.entity.Admin;
import project.service.AdminService;

@Controller
public class SecurityController {
    private final AdminService adminService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/login")
    public String showLogin(Model model){
        return "security/loginForm";
    }
}
