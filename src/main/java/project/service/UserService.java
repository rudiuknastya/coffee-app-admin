package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.userModel.UserDTO;
import project.entity.User;
import project.entity.UserStatus;
import project.model.userModel.UserRequest;
import project.model.userModel.UserResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<UserDTO> getUsers(Pageable pageable);
    Page<UserDTO> searchUser(String phone, UserStatus status, LocalDate date, Pageable pageable);
    UserResponse getUserResponseById(Long id);
    List<Long> getLanguagePercentages();
    Long getUsersCount();
    void updateUser(UserRequest userRequest);
    void deleteUser(Long id);
}
