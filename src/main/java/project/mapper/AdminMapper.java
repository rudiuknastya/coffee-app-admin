package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.dto.AdminDTO;
import project.entity.Admin;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminMapper ADMIN_MAPPER = Mappers.getMapper(AdminMapper.class);
    List<AdminDTO> adminListToAdminDTOList(List<Admin> admins);
}
