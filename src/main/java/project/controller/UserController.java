package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.model.UserDTO;
import project.entity.Language;
import project.entity.User;
import project.entity.UserStatus;
import project.service.UserService;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    private int pageSize = 1;
    @GetMapping("/admin/users")
    public String showUsers(Model model){
        model.addAttribute("pageNum", 10);
        model.addAttribute("status", UserStatus.values());
        return "user/users";
    }
    @GetMapping("/admin/getUsers")
    public @ResponseBody Page<UserDTO> getUsers(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return userService.getUsers(pageable);
    }
    @GetMapping("/admin/searchUsers")
    public @ResponseBody Page<UserDTO> searchUsers(@RequestParam("page")int page, @RequestParam(name="searchValue", required = false) String phone, @RequestParam(name="status", required = false) UserStatus status){
        Pageable pageable = PageRequest.of(page, pageSize);
        return userService.searchUser(phone, status, pageable);
    }
    @GetMapping("/admin/deleteUser/{id}")
    public @ResponseBody String deleteUser(@PathVariable Long id){
        User user = userService.getUserById(id);
        user.setDeleted(true);
        userService.saveUser(user);
        return "success";
    }
    @GetMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model){
        String l = "edit/"+id;
        model.addAttribute("pageNum", 10);
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("link", l);
        model.addAttribute("status", UserStatus.values());
        model.addAttribute("languages", Language.values());
        return "user/user_page";
    }
    @PostMapping("/admin/users/edit/{id}")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()) {
            String l = "edit/" + user.getId();
            model.addAttribute("pageNum", 10);
            model.addAttribute("link", l);
            model.addAttribute("status", UserStatus.values());
            model.addAttribute("languages", Language.values());
            return "user/user_page";
        }
        User userInDB = userService.getUserById(user.getId());
        userInDB.setName(user.getName());
        userInDB.setPhoneNumber(user.getPhoneNumber());
        userInDB.setLanguage(user.getLanguage());
        userInDB.setBirthDate(user.getBirthDate());
        userInDB.setStatus(user.getStatus());
        userService.saveUser(userInDB);
        return "redirect:/admin/users";
    }

}
