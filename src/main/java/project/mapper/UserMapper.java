package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.model.userModel.LanguageDTO;
import project.model.userModel.UserDTO;
import project.entity.User;
import project.model.userModel.UserResponse;
import project.model.userModel.UserStatusDTO;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
    @Named("userListToUserDtoList")
    static List<UserDTO> userListToUserDtoList(List<User> users){
        if(users == null){
            return null;
        }
        List<UserDTO> userDTOS = new ArrayList<>(users.size());
        for(User user: users){
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setBirthDate(user.getBirthDate());
            userDTO.setStatus(user.getStatus().getStatusName());
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }
    @Named("userToUserRequest")
    static UserResponse userToUserRequest(User user){
        if(user == null){
            return null;
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setBirthDate(user.getBirthDate());
        LanguageDTO languageDTO = new LanguageDTO();
        languageDTO.setLanguage(user.getLanguage());
        languageDTO.setName(user.getLanguage().getLanguageName());
        userResponse.setLanguage(languageDTO);
        UserStatusDTO userStatusDTO = new UserStatusDTO();
        userStatusDTO.setUserStatus(user.getStatus());
        userStatusDTO.setName(user.getStatus().getStatusName());
        userResponse.setStatus(userStatusDTO);
        return userResponse;
    }
}
