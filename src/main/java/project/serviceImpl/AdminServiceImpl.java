package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.model.adminModel.AdminDTO;
import project.entity.Admin;
import project.entity.Role;
import project.mapper.AdminMapper;
import project.model.adminModel.AdminRequest;
import project.model.adminModel.AdminResponse;
import project.model.adminModel.ProfileDTO;
import project.repository.AdminRepository;
import project.service.AdminService;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.AdminSpecification.*;


@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.adminRepository = adminRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<AdminDTO> getAdmins(Pageable pageable, String email) {
        logger.info("getAdmins() - Finding admins for page "+ pageable.getPageNumber());
        Page<Admin> admins = adminRepository.findAll(byEmailNot(email),pageable);
        List<AdminDTO> adminDTOS = AdminMapper.adminListToAdminDTOList(admins.getContent());
        Page<AdminDTO> adminDTOPage = new PageImpl<>(adminDTOS, pageable, admins.getTotalElements());
        logger.info("getAdmins() - Admins were found");
        return adminDTOPage;
    }

    @Override
    public Admin getAdminById(Long id) {
        logger.info("getAdminById() - Finding admin by id "+id);
        Admin admin = adminRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getAdminById() - Admin was found");
        return admin;
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        logger.info("saveAdmin() - Saving admin");
        Admin admin1 = adminRepository.save(admin);
        logger.info("saveAdmin() - Admin was saved");
        return admin1;
    }

    @Override
    public Page<AdminDTO> searchAdmins(String input, Role role,String email, Pageable pageable) {
        logger.info("searchAdmins() - Finding admins for input "+ input + "and role "+role);
        Page<Admin> admins;
        if(role != null  &&  (input == null || input.equals(""))) {
            admins = adminRepository.findAll(where(byRole(role).and(byEmailNot(email))),pageable);
        } else if((input != null && !input.equals(""))  && role == null){
            admins = adminRepository.findAll(where(byEmailNot(email).and(byEmailLike(input).or(byLastNameLike(input)))),pageable);
        } else if((input != null && !input.equals("")) && role != null) {
            admins = adminRepository.findAll(where(byRole(role).and(byEmailNot(email)).and(byLastNameLike(input).or(byEmailLike(input)))),pageable);
        } else {
            admins = adminRepository.findAll(pageable);
        }
        List<AdminDTO> adminDTOS = AdminMapper.adminListToAdminDTOList(admins.getContent());
        Page<AdminDTO> adminDTOPage = new PageImpl<>(adminDTOS, pageable, admins.getTotalElements());
        logger.info("searchAdmins() - Admins were found");
        return adminDTOPage;
    }

    @Override
    public void deleteAdmin(Long id) {
        logger.info("deleteAdmin() - Deleting admin by id "+id);
        adminRepository.deleteById(id);
        logger.info("deleteAdmin() - Admin was deleted");
    }

    @Override
    public AdminResponse getAdminResponseById(Long id) {
        logger.info("getAdminResponseById() - Finding admin for admin response by id "+id);
        Admin admin = adminRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        AdminResponse adminResponse = AdminMapper.adminToAdminResponse(admin);
        logger.info("getAdminResponseById() - Admin was found");
        return adminResponse;
    }

    @Override
    public Admin getAdminByEmail(String email) {
        logger.info("getAdminByEmail() - Finding admin by email "+email);
        Admin admin = adminRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        logger.info("getAdminByEmail() - Admin was found");
        return admin;
    }

    @Override
    public ProfileDTO getProfileResponseByEmail(String email) {
        logger.info("getAdminByEmail() - Finding admin for profile response by email "+email);
        Admin admin = adminRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        ProfileDTO profileDTO = AdminMapper.ADMIN_MAPPER.adminToProfileResponse(admin);
        logger.info("getAdminByEmail() - Admin was found");
        return profileDTO;
    }

    @Override
    public Admin getAdminByPassword(String password) {
        logger.info("getAdminByPassword() - Finding admin by password");
        Admin admin = adminRepository.findByPassword(password);
        logger.info("getAdminByPassword() - Admin was found");
        return admin;
    }

    @Override
    public void updateAdmin(AdminRequest adminRequest) {
        logger.info("updateAdmin() - Updating admin");
        Admin adminInDB = adminRepository.findById(adminRequest.getId()).orElseThrow(EntityNotFoundException::new);
        adminInDB.setFirstName(adminRequest.getFirstName());
        adminInDB.setLastName(adminRequest.getLastName());
        adminInDB.setEmail(adminRequest.getEmail());
        adminInDB.setCity(adminRequest.getCity());
        adminInDB.setBirthDate(adminRequest.getBirthDate());
        adminInDB.setRole(adminRequest.getRole());
        adminRepository.save(adminInDB);
        logger.info("updateAdmin() - Admin was updated");
    }

    @Override
    public void updateAdminProfile(ProfileDTO profileDTO, String newPassword, String confirmNewPassword, String oldPassword) {
        Admin admin = adminRepository.findById(profileDTO.getId()).orElseThrow(EntityNotFoundException::new);
        admin.setFirstName(profileDTO.getFirstName());
        admin.setLastName(profileDTO.getLastName());
        admin.setEmail(profileDTO.getEmail());
        admin.setBirthDate(profileDTO.getBirthDate());
        admin.setCity(profileDTO.getCity());
        if(!newPassword.equals("") && !confirmNewPassword.equals("") && !oldPassword.equals("") && newPassword.equals(confirmNewPassword)){
            admin.setPassword(bCryptPasswordEncoder.encode(newPassword));
        }
        adminRepository.save(admin);
    }
}
