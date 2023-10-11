package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.entity.Additive;

import java.util.List;

public interface AdditiveRepository extends JpaRepository<Additive, Long>, JpaSpecificationExecutor<Additive> {
}
