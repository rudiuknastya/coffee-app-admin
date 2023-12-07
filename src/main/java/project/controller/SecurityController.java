package project.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project.entity.Admin;
import project.entity.PasswordResetToken;
import project.service.AdminService;
import project.service.MailService;
import project.service.PasswordResetTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class SecurityController {
    private final AdminService adminService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final MailService mailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityController(AdminService adminService, PasswordResetTokenService passwordResetTokenService, MailService mailService) {
        this.adminService = adminService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.mailService = mailService;
    }

    @GetMapping("/login")
    public String showLogin(){
        if(adminService.getAdminsCount().equals(0L)){
            adminService.createAdmin();
        }
        return "security/loginForm";
    }
    @GetMapping("/confirmation")
    public String confirmSending(){
        return "security/confirmation";
    }
    @GetMapping("/forgotPassword")
    public String forgotPassword(){
        return "security/forgotPassword";
    }
    @GetMapping("/changePassword")
    public String changePassword(@RequestParam("token")String token, Model model){
        model.addAttribute("token", token);
        if(passwordResetTokenService.validatePasswordResetToken(token)){
            return "security/changePassword";
        } else {
            return "security/expired";
        }
    }
    @GetMapping("/success")
    public String success(){
        return "security/successful";
    }
    @PostMapping("/changePassword")
    public @ResponseBody String setNewPassword(@RequestParam("token")String token,@RequestParam("password")String password, Model model){
        if(passwordResetTokenService.validatePasswordResetToken(token)){
            String encodedPassword = bCryptPasswordEncoder.encode(password);
            PasswordResetToken passwordResetToken = passwordResetTokenService.getPasswordResetToken(token);
            passwordResetToken.getAdmin().setPassword(encodedPassword);
            passwordResetTokenService.savePasswordResetToken(passwordResetToken);
            return "success";
        } else {
            return "wrong";
        }
    }
    @PostMapping("/resetPassword")
    public @ResponseBody String resetPassword(HttpServletRequest request, @RequestParam("email")String email){
        if(email.equals("")){
            return "blank";
        }
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        if(!admin.isPresent()){
            return "wrong";
        }
        String token = passwordResetTokenService.createAndSavePasswordResetToken(admin.get());
        mailService.sendToken(token,email,request);
        return "success";
    }
}
