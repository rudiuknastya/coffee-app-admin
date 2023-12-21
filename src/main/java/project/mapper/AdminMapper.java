package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.Role;
import project.model.adminModel.*;
import project.entity.Admin;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminMapper ADMIN_MAPPER = Mappers.getMapper(AdminMapper.class);
    ProfileResponse adminToProfileResponse(Admin admin);
    Admin saveAdminRequestToAdmin(SaveAdminRequest saveAdminRequest);
    List<AdminDTO> adminListToAdminDtoList(List<Admin> admins);
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "email")
    void setProfileRequest(@MappingTarget Admin admin, ProfileRequest profileRequest);
    @Mapping(ignore = true, target = "id")
    void setAdminRequest(@MappingTarget Admin admin, AdminRequest adminRequest);
    @Mapping(target = "role", expression = "java(admin.getRole().getRoleName())")
    AdminDTO adminToAdminDTO(Admin admin);

    @Mapping(target = "role", expression = "java(createRoleDTO(admin))")
    AdminResponse adminToAdminResponse(Admin admin);
    default RoleDTO createRoleDTO(Admin admin) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRole(admin.getRole());
        roleDTO.setName(admin.getRole().getRoleName());
        return roleDTO;
    }
}
