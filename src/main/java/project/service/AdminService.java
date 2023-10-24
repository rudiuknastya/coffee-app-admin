package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.adminModel.AdminDTO;
import project.entity.Admin;
import project.entity.Role;
import project.model.adminModel.AdminResponse;

public interface AdminService {
    Page<AdminDTO> getAdmins(Pageable pageable);
    Admin getAdminById(Long id);
    Admin getAdminByEmail(String email);
    Admin saveAdmin(Admin admin);
    AdminResponse getAdminResponseById(Long id);
    Page<AdminDTO> searchAdmins(String input, Role role, Pageable pageable);
    void deleteAdmin(Long id);
}
