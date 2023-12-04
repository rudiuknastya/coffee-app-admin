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
import project.model.userModel.*;
import project.entity.Language;
import project.entity.User;
import project.entity.UserStatus;
import project.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    private int pageSize = 1;
    @GetMapping("/users")
    public String showUsers(Model model){
        model.addAttribute("pageNum", 10);
        model.addAttribute("status", UserStatus.values());
        return "user/users";
    }
    @GetMapping("/getUsers")
    public @ResponseBody Page<UserDTO> getUsers(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return userService.getUsers(pageable);
    }
    @GetMapping("/searchUsers")
    public @ResponseBody Page<UserDTO> searchUsers(@RequestParam("page")int page, @RequestParam(name="searchValue", required = false) String phone, @RequestParam(name="status", required = false) UserStatus status){
        Pageable pageable = PageRequest.of(page, pageSize);
        return userService.searchUser(phone, status, pageable);
    }
    @GetMapping("/deleteUser/{id}")
    public @ResponseBody ResponseEntity deleteUser(@PathVariable Long id){
        User user = userService.getUserById(id);
        user.setDeleted(true);
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model){
        model.addAttribute("pageNum", 10);
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
    public @ResponseBody List<FieldError> updateUser(@Valid @ModelAttribute("editUser") UserRequest user, BindingResult bindingResult){
        User user1 = userService.getUserByPhoneNumber(user.getPhoneNumber());
        if(bindingResult.hasErrors() ) {
            List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
            if(user1 != null && user1.getId() != user.getId()){
                FieldError fieldError = new FieldError("Number exist","phoneNumber","Такий номер телефону вже існує");
                fieldErrors.add(fieldError);
            }
            return fieldErrors;
        }
        if(user1 != null && user1.getId() != user.getId()){
            FieldError fieldError = new FieldError("Number exist","phoneNumber","Такий номер телефону вже існує");
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(fieldError);
            return fieldErrors;
        }
        userService.updateUser(user);
        return null;
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
