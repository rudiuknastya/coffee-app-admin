package project.serviceImpl;

import jakarta.persistence.EntityExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.AdminDTO;
import project.entity.Admin;
import project.entity.Role;
import project.mapper.AdminMapper;
import project.repository.AdminRepository;
import project.service.AdminService;
import project.specifications.UserSpecification;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.AdminSpecification.*;


@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<AdminDTO> getAdmins(Pageable pageable) {
        logger.info("getAdmins() - Finding admins for page "+ pageable.getPageNumber());
        Page<Admin> admins = adminRepository.findAll(pageable);
        List<AdminDTO> adminDTOS = AdminMapper.ADMIN_MAPPER.adminListToAdminDTOList(admins.getContent());
        Page<AdminDTO> adminDTOPage = new PageImpl<>(adminDTOS, pageable, admins.getTotalElements());
        logger.info("getAdmins() - Admins were found");
        return adminDTOPage;
    }

    @Override
    public Admin getAdminById(Long id) {
        logger.info("getAdminById() - Finding admin by id "+id);
        Admin admin = adminRepository.findById(id).orElseThrow(EntityExistsException::new);
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
    public Page<AdminDTO> searchAdmins(String input, Role role, Pageable pageable) {
        logger.info("searchAdmins() - Finding admins for input "+ input + "and role "+role);
        Page<Admin> admins;
        if(role != null  &&  (input == null || input.equals(""))) {
            admins = adminRepository.findAll(where(byRole(role)),pageable);
        } else if((input != null && !input.equals(""))  && role == null){
            admins = adminRepository.findAll(where(byEmailLike(input).or(byLastNameLike(input))),pageable);
        } else if((input != null && !input.equals("")) && role != null) {
            admins = adminRepository.findAll(where(byRole(role).and(byLastNameLike(input).or(byEmailLike(input)))),pageable);
        } else {
            admins = adminRepository.findAll(pageable);
        }
        List<AdminDTO> adminDTOS = AdminMapper.ADMIN_MAPPER.adminListToAdminDTOList(admins.getContent());
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
}
