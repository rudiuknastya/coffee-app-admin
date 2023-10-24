package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {
    Admin findByEmail(String email);
}
