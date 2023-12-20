package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import project.entity.Admin;
import project.entity.Role;
import project.mapper.AdminMapper;
import project.model.adminModel.*;
import project.repository.AdminRepository;
import project.service.AdminService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    @Value("${upload.path}")
    private String uploadPath;
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
            admins = adminRepository.findAll(byEmailNot(email),pageable);
        }
        List<AdminDTO> adminDTOS = AdminMapper.adminListToAdminDTOList(admins.getContent());
        Page<AdminDTO> adminDTOPage = new PageImpl<>(adminDTOS, pageable, admins.getTotalElements());
        logger.info("searchAdmins() - Admins were found");
        return adminDTOPage;
    }

    @Override
    public void deleteAdmin(Long id) {
        logger.info("deleteAdmin() - Deleting admin by id "+id);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Admin admin = adminRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if(admin.getEmail().equals(email) || (admin.getRoot() != null && !admin.getRoot())){
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        } else {
            adminRepository.deleteById(id);
        }
        logger.info("deleteAdmin() - Admin was deleted");
    }

    @Override
    public AdminResponse getAdminResponseById(Long id) {
        logger.info("getAdminResponseById() - Finding admin for admin response by id "+id);
        Admin admin = adminRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        AdminResponse adminResponse = null;
        if(admin.getRoot() == null){
            adminResponse = AdminMapper.adminToAdminResponse(admin);
        }
        logger.info("getAdminResponseById() - Admin was found");
        return adminResponse;
    }

    @Override
    public Optional<Admin> getAdminByEmail(String email) {
        logger.info("getAdminByEmail() - Finding admin by email "+email);
        Optional<Admin> admin = adminRepository.findByEmail(email);
        logger.info("getAdminByEmail() - Admin was found");
        return admin;
    }

    @Override
    public ProfileResponse getProfileResponseByEmail(String email) {
        logger.info("getAdminByEmail() - Finding admin for profile response by email "+email);
        Admin admin = adminRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        ProfileResponse profileResponse = AdminMapper.ADMIN_MAPPER.adminToProfileResponse(admin);
        logger.info("getAdminByEmail() - Admin was found");
        return profileResponse;
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
    public void updateAdminProfile(ProfileRequest profileRequest, MultipartFile file) throws IOException {
        logger.info("updateAdminProfile() - Updating admin profile");
        Admin admin = adminRepository.findById(profileRequest.getId()).orElseThrow(EntityNotFoundException::new);
        admin.setFirstName(profileRequest.getFirstName());
        admin.setLastName(profileRequest.getLastName());
        admin.setBirthDate(profileRequest.getBirthDate());
        admin.setCity(profileRequest.getCity());
        if(file != null) {
            updateImage(file, admin);
        }
        if(admin.getRoot() == null){
            admin.setEmail(profileRequest.getEmail());
            if(!profileRequest.getNewPassword().equals("") && !profileRequest.getConfirmNewPassword().equals("") && !profileRequest.getOldPassword().equals("") && profileRequest.getNewPassword().equals(profileRequest.getConfirmNewPassword())){
                admin.setPassword(bCryptPasswordEncoder.encode(profileRequest.getNewPassword()));
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AdminDetails adminDetails = (AdminDetails) authentication.getPrincipal();
        adminDetails.getAdmin().setFirstName(profileRequest.getFirstName());
        adminDetails.getAdmin().setLastName(profileRequest.getLastName());
        adminRepository.save(admin);
        logger.info("updateAdminProfile() - Admin profile was updated");
    }

    @Override
    public Long getAdminsCount() {
        logger.info("getAdminsCount() - Finding admins count");
        Long count = adminRepository.count();
        logger.info("getAdminsCount() - Admins count was found");
        return count;
    }

    @Override
    public void createFirstAdmin() {
        logger.info("createFirstAdmin() - Creating first admin");
        Admin admin = new Admin();
        admin.setEmail("admin@gmail.com");
        admin.setPassword(bCryptPasswordEncoder.encode("admin"));
        admin.setRole(Role.ADMIN);
        admin.setFirstName("");
        admin.setLastName("");
        admin.setCity("");
        admin.setBirthDate(LocalDate.now());
        saveImage(admin);
        adminRepository.save(admin);
        logger.info("createFirstAdmin() - First admin was created");
    }

    @Override
    public void createAndSaveAdmin(SaveAdminRequest adminRequest) {
        logger.info("createAndSaveAdmin() - Creating and saving admin");
        Admin admin = AdminMapper.ADMIN_MAPPER.saveAdminRequestToAdmin(adminRequest);
        admin.setPassword(bCryptPasswordEncoder.encode(adminRequest.getNewPassword()));
        saveImage(admin);
        adminRepository.save(admin);
        logger.info("createAndSaveAdmin() - Admin was created and saved");
    }

    private void updateImage(MultipartFile image, Admin admin) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String uuidFile = UUID.randomUUID().toString();
        String uniqueName = uuidFile+"."+image.getOriginalFilename();
        String name = admin.getImage();
        admin.setImage(uniqueName);
        Path path = Paths.get(uploadPath+"/"+uniqueName);
        image.transferTo(new File(path.toUri()));
        File file = new File(uploadPath+"/"+name);
        file.delete();
    }
    private void saveImage(Admin admin){
        Path path = Paths.get("src/main/resources/static/assets/img/avatars/1.png");
        File file = new File(path.toAbsolutePath().toString());
        String uuidFile = UUID.randomUUID().toString();
        String uniqueName = uuidFile+"."+file.getName();
        File copyDirectory = new File(uploadPath);
        File newFile = new File(uploadPath+"/"+uniqueName);
        admin.setImage(uniqueName);
        try {
            FileUtils.copyFileToDirectory(file, copyDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File copiedFile = new File(uploadPath+"/1.png");
        copiedFile.renameTo(newFile);
    }
}
