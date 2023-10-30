package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.model.userModel.UserDTO;
import project.entity.User;
import project.entity.UserStatus;
import project.mapper.UserMapper;
import project.model.userModel.UserResponse;
import project.repository.UserRepository;
import project.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.UserSpecification.*;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<UserDTO> getUsers(Pageable pageable) {
        logger.info("getUsers() - Finding all users for page "+ pageable.getPageNumber());
        Page<User> users = userRepository.findAll(byDeleted(), pageable);
        List<UserDTO> userDTOS = UserMapper.userListToUserDtoList(users.getContent());
        Page<UserDTO> userDTOPage = new PageImpl<>(userDTOS, pageable, users.getTotalElements());
        logger.info("getUsers() - All users were found");
        return userDTOPage;
    }

    @Override
    public User getUserById(Long id) {
        logger.info("getUserById() - Finding user by id "+id);
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getUserById() - User was found");
        return user;
    }

    @Override
    public User saveUser(User user) {
        logger.info("saveUser() - Saving user");
        User user1 = userRepository.save(user);
        logger.info("saveUser() - User was saved");
        return user1;
    }

    @Override
    public Page<UserDTO> searchUser(String phone, UserStatus status, Pageable pageable) {
        logger.info("searchUser() - Finding users for phone "+ phone + " and status "+status);
        Page<User> users;
        if(status != null  &&  (phone == null || phone.equals(""))) {
            users = userRepository.findAll(where(byStatus(status).and(byDeleted())),pageable);
        } else if((phone != null && !phone.equals(""))  && status == null){
            users = userRepository.findAll(where(byPhoneNumberLike(phone).and(byDeleted())),pageable);
        } else if((phone != null && !phone.equals("")) && status != null) {
            users = userRepository.findAll(where(byStatus(status).and(byPhoneNumberLike(phone).and(byDeleted()))),pageable);
        } else {
            users = userRepository.findAll(byDeleted(),pageable);
        }
        List<UserDTO> userDTOS = UserMapper.userListToUserDtoList(users.getContent());
        Page<UserDTO> userDTOPage = new PageImpl<>(userDTOS, pageable, users.getTotalElements());
        logger.info("searchUser() - Users were found");
        return userDTOPage;
    }

    @Override
    public User getUserWithProducts(Long id) {
        logger.info("getUserWithProducts() - Finding user with products by id "+id);
        User user = userRepository.findUserWithProductsById(id);
        logger.info("getUserWithProducts() - User was found");
        return user;
    }

    @Override
    public UserResponse getUserResponseById(Long id) {
        logger.info("getUserResponseById() - Finding user for user response by id "+id);
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        UserResponse userRequest = UserMapper.userToUserRequest(user);
        logger.info("getUserResponseById() - User was found");
        return userRequest;
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        logger.info("getUserByPhoneNumber() - Finding user by phoneNumber "+phoneNumber);
        User user = userRepository.findByPhoneNumber(phoneNumber);
        logger.info("getUserByPhoneNumber() - User was found");
        return user;
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
}
