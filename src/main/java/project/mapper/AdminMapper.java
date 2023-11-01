package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.model.adminModel.AdminDTO;
import project.entity.Admin;
import project.model.adminModel.AdminResponse;
import project.model.adminModel.ProfileResponse;
import project.model.adminModel.RoleDTO;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminMapper ADMIN_MAPPER = Mappers.getMapper(AdminMapper.class);
    ProfileResponse adminToProfileResponse(Admin admin);
    @Named("adminListToAdminDTOList")
    static List<AdminDTO> adminListToAdminDTOList(List<Admin> admins){
        if(admins == null){
            return null;
        }
        List<AdminDTO> adminDTOS = new ArrayList<>(admins.size());
        for(Admin admin: admins){
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setId(admin.getId());
            adminDTO.setFirstName(admin.getFirstName());
            adminDTO.setLastName(admin.getLastName());
            adminDTO.setEmail(admin.getEmail());
            adminDTO.setRole(admin.getRole().getRoleName());
            adminDTOS.add(adminDTO);
        }
        return adminDTOS;
    }
    @Named("adminListToAdminDTOList")
    static AdminResponse adminToAdminResponse(Admin admin) {
        if(admin == null){
            return null;
        }
        AdminResponse adminResponse = new AdminResponse();
        adminResponse.setId(admin.getId());
        adminResponse.setFirstName(admin.getFirstName());
        adminResponse.setLastName(admin.getLastName());
        adminResponse.setEmail(admin.getEmail());
        adminResponse.setCity(admin.getCity());
        adminResponse.setBirthDate(admin.getBirthDate());
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRole(admin.getRole());
        roleDTO.setName(admin.getRole().getRoleName());
        adminResponse.setRole(roleDTO);
        return adminResponse;
    }
}
