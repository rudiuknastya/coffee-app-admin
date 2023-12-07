package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.userModel.UserDTO;
import project.entity.User;
import project.entity.UserStatus;
import project.model.userModel.UserRequest;
import project.model.userModel.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<UserDTO> getUsers(Pageable pageable);
    User getUserById(Long id);
    User saveUser(User user);
    Page<UserDTO> searchUser(String phone, UserStatus status, Pageable pageable);
    User getUserWithProducts(Long id);
    UserResponse getUserResponseById(Long id);
    Optional<User> getUserByPhoneNumber(String phoneNumber);
    List<Long> getLanguagePercentages();
    Long getUsersCount();
    void updateUser(UserRequest userRequest);
    void deleteUser(Long id);
    Optional<User> getUserByEmail(String email);
}
