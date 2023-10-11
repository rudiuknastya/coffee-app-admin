package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.dto.AdditiveTypeDTO;
import project.dto.CategoryDTO;
import project.entity.AdditiveType;

import java.util.List;

public interface AdditiveTypeRepository extends JpaRepository<AdditiveType, Long> {
    @Query(value = "select additive_type.id as id, additive_type.name as name, count(additive.id) as additiveQuantity, additive_type.status as status from additive_type left join additive on additive_type.id = additive.additive_type_id where additive_type.deleted = 0 group by additive_type.id", nativeQuery = true)
    Page<AdditiveTypeDTO> findAdditiveTypes(Pageable pageable);

    @Query(value = "select additive_type.id as id, additive_type.name as name, count(additive.id) as additiveQuantity, additive_type.status as status from additive_type left join additive on additive_type.id = additive.additive_type_id where additive_type.deleted = 0 and upper(additive_type.name) like :additType group by additive_type.id", nativeQuery = true)
    Page<AdditiveTypeDTO> searchAdditiveTypes(@Param("additType")String name, Pageable pageable);
    List<AdditiveType> findByDeletedNot(Boolean deleted);
}
