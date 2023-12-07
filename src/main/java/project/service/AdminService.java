package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.adminModel.AdminDTO;
import project.entity.Admin;
import project.entity.Role;
import project.model.adminModel.AdminRequest;
import project.model.adminModel.AdminResponse;
import project.model.adminModel.ProfileDTO;

import java.util.Optional;

public interface AdminService {
    Page<AdminDTO> getAdmins(Pageable pageable, String email);
    Admin getAdminById(Long id);
    Optional<Admin> getAdminByEmail(String email);
    Admin saveAdmin(Admin admin);
    Admin getAdminByPassword(String password);
    AdminResponse getAdminResponseById(Long id);
    ProfileDTO getProfileResponseByEmail(String email);
    Page<AdminDTO> searchAdmins(String input, Role role,String email, Pageable pageable);
    void deleteAdmin(Long id);
    void updateAdmin(AdminRequest adminRequest);
    void updateAdminProfile(ProfileDTO profileDTO, String newPassword, String confirmNewPassword, String oldPassword);
    Long getAdminsCount();
    void createAdmin();
}
