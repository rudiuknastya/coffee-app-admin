package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.Admin;
import project.model.adminModel.RoleDTO;
import project.model.userModel.*;
import project.entity.User;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
    List<UserDTO> userListToUserDtoList(List<User> users);
    @Mapping(target = "status", expression = "java(user.getStatus().getStatusName())")
    UserDTO userToUserDTO(User user);
    @Mapping(ignore = true, target = "id")
    void setUserRequest(@MappingTarget User user, UserRequest userRequest);
    @Mapping(target = "language", expression = "java(createLanguageDTO(user))")
    @Mapping(target = "status", expression = "java(createStatusDTO(user))")
    UserResponse userToUserRequest(User user);
    default LanguageDTO createLanguageDTO(User user) {
        LanguageDTO languageDTO = new LanguageDTO();
        languageDTO.setLanguage(user.getLanguage());
        languageDTO.setName(user.getLanguage().getLanguageName());
        return languageDTO;
    }
    default UserStatusDTO createStatusDTO(User user) {
        UserStatusDTO userStatusDTO = new UserStatusDTO();
        userStatusDTO.setUserStatus(user.getStatus());
        userStatusDTO.setName(user.getStatus().getStatusName());
        return userStatusDTO;
    }
}
