package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.model.UserDTO;
import project.entity.User;
import project.entity.UserStatus;
import project.mapper.UserMapper;
import project.repository.UserRepository;
import project.service.UserService;

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
        List<UserDTO> userDTOS = UserMapper.USER_MAPPER.userListToUserDtoList(users.getContent());
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
        List<UserDTO> userDTOS = UserMapper.USER_MAPPER.userListToUserDtoList(users.getContent());
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
}
