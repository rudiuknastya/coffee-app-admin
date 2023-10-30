package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.userModel.UserDTO;
import project.entity.User;
import project.entity.UserStatus;
import project.model.userModel.UserResponse;

import java.util.List;

public interface UserService {
    Page<UserDTO> getUsers(Pageable pageable);
    User getUserById(Long id);
    User saveUser(User user);
    Page<UserDTO> searchUser(String phone, UserStatus status, Pageable pageable);
    User getUserWithProducts(Long id);
    UserResponse getUserResponseById(Long id);
    User getUserByPhoneNumber(String phoneNumber);
    List<Long> getLanguagePercentages();
    Long getUsersCount();
}
