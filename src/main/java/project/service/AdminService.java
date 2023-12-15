package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import project.model.adminModel.*;
import project.entity.Admin;
import project.entity.Role;

import java.io.IOException;
import java.util.Optional;

public interface AdminService {
    Page<AdminDTO> getAdmins(Pageable pageable, String email);
    Admin getAdminById(Long id);
    Optional<Admin> getAdminByEmail(String email);
    Admin saveAdmin(Admin admin);
    Admin getAdminByPassword(String password);
    AdminResponse getAdminResponseById(Long id);
    ProfileResponse getProfileResponseByEmail(String email);
    Page<AdminDTO> searchAdmins(String input, Role role,String email, Pageable pageable);
    void deleteAdmin(Long id);
    void updateAdmin(AdminRequest adminRequest);
    void updateAdminProfile(ProfileRequest profileRequest, MultipartFile file) throws IOException;
    Long getAdminsCount();
    void createFirstAdmin();
    void createAndSaveAdmin(SaveAdminRequest adminRequest);
}
