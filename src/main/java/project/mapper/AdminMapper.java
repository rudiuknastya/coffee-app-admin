package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.model.AdminDTO;
import project.entity.Admin;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
//    AdminMapper ADMIN_MAPPER = Mappers.getMapper(AdminMapper.class);
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
}
