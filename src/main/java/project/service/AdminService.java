package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.AdminDTO;
import project.entity.Admin;
import project.entity.Role;

public interface AdminService {
    Page<AdminDTO> getAdmins(Pageable pageable);
    Admin getAdminById(Long id);
    Admin saveAdmin(Admin admin);
    Page<AdminDTO> searchAdmins(String input, Role role, Pageable pageable);
    void deleteAdmin(Long id);
}
