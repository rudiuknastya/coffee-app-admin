package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import project.model.userModel.UserDTO;
import project.entity.User;
import project.entity.UserStatus;
import project.mapper.UserMapper;
import project.model.userModel.UserRequest;
import project.model.userModel.UserResponse;
import project.repository.UserRepository;
import project.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.UserSpecification.*;
@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LogManager.getLogger("serviceLogger");
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserDTO> getUsers(Pageable pageable) {
        logger.info("getUsers() - Finding all users for page "+ pageable.getPageNumber());
        Page<User> users = userRepository.findAll(byDeleted(), pageable);
        List<UserDTO> userDTOS = userMapper.userListToUserDtoList(users.getContent());
        Page<UserDTO> userDTOPage = new PageImpl<>(userDTOS, pageable, users.getTotalElements());
        logger.info("getUsers() - All users were found");
        return userDTOPage;
    }

    @Override
    public Page<UserDTO> searchUser(String phone, UserStatus status, LocalDate date, Pageable pageable) {
        logger.info("searchUser() - Finding users for phone "+ phone + " and status "+status);
        Page<User> users = filterUsers(phone, status, date, pageable);
        List<UserDTO> userDTOS = userMapper.userListToUserDtoList(users.getContent());
        Page<UserDTO> userDTOPage = new PageImpl<>(userDTOS, pageable, users.getTotalElements());
        logger.info("searchUser() - Users were found");
        return userDTOPage;
    }
    private Page<User> filterUsers(String phone, UserStatus status, LocalDate date, Pageable pageable){
        Specification<User> specification = Specification.where(byDeleted());
        if(phone != null){
            specification = specification.and(byPhoneNumberLike(phone));
        } if(status != null){
            specification = specification.and(byStatus(status));
        }
        if(date != null){
            specification = specification.and(byBirthDate(date));
        }
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public UserResponse getUserResponseById(Long id) {
        logger.info("getUserResponseById() - Finding user for user response by id "+id);
        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User was not found by id "+id));
        UserResponse userRequest = userMapper.userToUserRequest(user);
        logger.info("getUserResponseById() - User was found");
        return userRequest;
    }

    @Override
    public List<Long> getLanguagePercentages() {
        logger.info("getLanguagePercentages() - Finding percentages of user languages");
        List<Long> percentages = new ArrayList<>(3);
        percentages.add(userRepository.findPercentageOfUkr());
        percentages.add(userRepository.findPercentageOfEng());
        percentages.add(userRepository.findPercentageOfSpa());
        logger.info("getLanguagePercentages() - Percentages of user languages were found");
        return percentages;
    }

    @Override
    public Long getUsersCount() {
        logger.info("getUsersCount() - Finding users count");
        Long count = userRepository.findUsersCount();
        logger.info("getUsersCount() - Users count was found");
        return count;
    }

    @Override
    public void updateUser(UserRequest userRequest) {
        logger.info("updateUser() - Updating user");
        User userInDB = userRepository.findById(userRequest.id()).orElseThrow(()-> new EntityNotFoundException("User was not found by id "+userRequest.id()));
        userMapper.setUserRequest(userInDB,userRequest);
        userRepository.save(userInDB);
        logger.info("updateUser() - User was updated");
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("deleteUser() - Deleting user");
        User user = userRepository.findUserWithProductsById(id);
        user.setDeleted(true);
        user.getProducts().clear();
        userRepository.save(user);
        logger.info("deleteUser() - User was deleted");
    }
}
