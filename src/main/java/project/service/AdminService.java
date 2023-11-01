package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.adminModel.AdminDTO;
import project.entity.Admin;
import project.entity.Role;
import project.model.adminModel.AdminResponse;
import project.model.adminModel.ProfileResponse;

public interface AdminService {
    Page<AdminDTO> getAdmins(Pageable pageable, String email);
    Admin getAdminById(Long id);
    Admin getAdminByEmail(String email);
    Admin saveAdmin(Admin admin);
    Admin getAdminByPassword(String password);
    AdminResponse getAdminResponseById(Long id);
    ProfileResponse getProfileResponseByEmail(String email);
    Page<AdminDTO> searchAdmins(String input, Role role, Pageable pageable);
    void deleteAdmin(Long id);
}
