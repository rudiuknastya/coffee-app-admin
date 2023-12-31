package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.entity.Admin;
import project.entity.Language;
import project.entity.UserStatus;
import project.model.userModel.*;
import project.service.AdminService;
import project.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;
    private final AdminService adminService;

    public UserController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }
    @GetMapping("/users")
    public String showUsers(Model model){
        model.addAttribute("pageNum", 10);
        model.addAttribute("status", UserStatus.values());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "user/users";
    }
    @GetMapping("/getUsers")
    public @ResponseBody Page<UserDTO> getUsers(@RequestParam("page")int page,@RequestParam("size")int size){
        Pageable pageable = PageRequest.of(page, size);
        return userService.getUsers(pageable);
    }
    @GetMapping("/searchUsers")
    public @ResponseBody Page<UserDTO> searchUsers(@RequestParam("page")int page,@RequestParam("size")int size, @RequestParam(name="searchValue", required = false) String phone, @RequestParam(name="status", required = false) UserStatus status, @RequestParam(name="date", required = false) LocalDate date){
        Pageable pageable = PageRequest.of(page, size);
        return userService.searchUser(phone, status, date, pageable);
    }
    @GetMapping("/deleteUser/{id}")
    public @ResponseBody ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model){
        model.addAttribute("pageNum", 10);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "user/user_page";
    }
    @GetMapping("/users/edit/getUser/{id}")
    public @ResponseBody UserResponse getUser(@PathVariable Long id){
        return userService.getUserResponseById(id);
    }
    @GetMapping("/users/getUserStatuses")
    public @ResponseBody UserStatusDTO[] getUserStatuses(){
        UserStatus[] userStatuses = UserStatus.values();
        UserStatusDTO[] userStatusDTOs = new UserStatusDTO[userStatuses.length];
        for(int i = 0; i < userStatuses.length; i++){
            UserStatusDTO userStatusDTO = new UserStatusDTO();
            userStatusDTO.setUserStatus(userStatuses[i]);
            userStatusDTO.setName(userStatuses[i].getStatusName());
            userStatusDTOs[i] = userStatusDTO;
        }
        return userStatusDTOs;
    }

    @GetMapping("/users/getUserLanguages")
    public @ResponseBody LanguageDTO[] getUserLanguages(){
        Language[] languages = Language.values();
        LanguageDTO[] languageDTOS = new LanguageDTO[languages.length];
        for(int i = 0; i < languages.length; i++){
            LanguageDTO languageDTO = new LanguageDTO();
            languageDTO.setLanguage(languages[i]);
            languageDTO.setName(languages[i].getLanguageName());
            languageDTOS[i] = languageDTO;
        }
        return languageDTOS;
    }
    @PostMapping("/users/edit/editUser")
    public @ResponseBody ResponseEntity<?> updateUser(@Valid @ModelAttribute("editUser") UserRequest userRequest){
        userService.updateUser(userRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getUserLanguagePercentages")
    public @ResponseBody List<Long> getUserLanguagePercentages(){
        return userService.getLanguagePercentages();
    }
    @GetMapping("/getUsersCount")
    public @ResponseBody Long getUsersCount(){
        return userService.getUsersCount();
    }

}
