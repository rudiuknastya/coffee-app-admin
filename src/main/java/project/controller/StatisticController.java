package project.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.entity.Admin;
import project.entity.OrderStatus;
import project.service.AdminService;

import java.util.Optional;

@Controller
public class StatisticController {
    private final AdminService adminService;

    public StatisticController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/statistic")
    public String statistic(Model model){
        model.addAttribute("pageNum", 1);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "statistic/statistic";
    }
}
