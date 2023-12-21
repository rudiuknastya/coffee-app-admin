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
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public Page<UserDTO> getUsers(Pageable pageable) {
        logger.info("getUsers() - Finding all users for page "+ pageable.getPageNumber());
        Page<User> users = userRepository.findAll(byDeleted(), pageable);
        List<UserDTO> userDTOS = UserMapper.USER_MAPPER.userListToUserDtoList(users.getContent());
        Page<UserDTO> userDTOPage = new PageImpl<>(userDTOS, pageable, users.getTotalElements());
        logger.info("getUsers() - All users were found");
        return userDTOPage;
    }

    @Override
    public Page<UserDTO> searchUser(String phone, UserStatus status, LocalDate date, Pageable pageable) {
        logger.info("searchUser() - Finding users for phone "+ phone + " and status "+status);
        Page<User> users;
        if(status != null  &&  (phone == null || phone.equals("")) && date == null) {
            users = userRepository.findAll(byStatus(status).and(byDeleted()),pageable);
        } else if((phone != null && !phone.equals(""))  && status == null && date == null) {
            users = userRepository.findAll(where(byPhoneNumberLike(phone).and(byDeleted())),pageable);
        } else if(date != null && status == null && (phone == null || phone.equals(""))) {
            users = userRepository.findAll(where(byBirthDate(date).and(byDeleted())),pageable);
        } else if(date != null  &&  (phone != null && !phone.equals("")) && status == null){
            users = userRepository.findAll(where(byPhoneNumberLike(phone).and(byBirthDate(date)).and(byDeleted())),pageable);
        } else if(date != null  &&  (phone == null || phone.equals("")) && status != null) {
            users = userRepository.findAll(where(byStatus(status).and(byBirthDate(date)).and(byDeleted())), pageable);
        } else if(status != null  &&  (phone != null && !phone.equals("")) && date == null) {
            users = userRepository.findAll(where(byStatus(status).and(byPhoneNumberLike(phone).and(byDeleted()))),pageable);
        } else if((phone != null && !phone.equals("")) && status != null && date != null) {
            users = userRepository.findAll(where(byStatus(status).and(byPhoneNumberLike(phone)).and(byBirthDate(date)).and(byDeleted())),pageable);
        } else {
            users = userRepository.findAll(byDeleted(),pageable);
        }
        List<UserDTO> userDTOS = UserMapper.USER_MAPPER.userListToUserDtoList(users.getContent());
        Page<UserDTO> userDTOPage = new PageImpl<>(userDTOS, pageable, users.getTotalElements());
        logger.info("searchUser() - Users were found");
        return userDTOPage;
    }

    @Override
    public UserResponse getUserResponseById(Long id) {
        logger.info("getUserResponseById() - Finding user for user response by id "+id);
        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User was not found by id "+id));
        UserResponse userRequest = UserMapper.USER_MAPPER.userToUserRequest(user);
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
        User userInDB = userRepository.findById(userRequest.getId()).orElseThrow(()-> new EntityNotFoundException("User was not found by id "+userRequest.getId()));
        UserMapper.USER_MAPPER.setUserRequest(userInDB,userRequest);
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
