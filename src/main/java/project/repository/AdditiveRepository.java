package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.Additive;

import java.util.List;

public interface AdditiveRepository extends JpaRepository<Additive, Long>, JpaSpecificationExecutor<Additive> {
    @Query(value = "SELECT additive.id, additive.name, additive.price, additive.deleted, additive.status, additive.additive_type_id  FROM additive INNER JOIN additive_type ON additive_type_id = additive_type.id  WHERE additive_type_id = :id AND additive.deleted=false", nativeQuery = true)
    List<Additive> findAdditivesForAdditiveType(@Param("id")Long id);
    @Query(value = "SELECT additive.id, additive.name, additive.price, additive.deleted, additive.status, additive.additive_type_id  FROM additive INNER JOIN additive_type ON additive_type_id = additive_type.id  WHERE additive_type_id = :id AND additive.deleted=false", nativeQuery = true)
    Page<Additive> findAdditivesForAdditiveType(@Param("id")Long id, Pageable pageable);
}
