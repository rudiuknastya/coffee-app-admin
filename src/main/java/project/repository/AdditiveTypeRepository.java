package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.model.additiveTypeModel.AdditiveTypeDTO;
import project.entity.AdditiveType;

public interface AdditiveTypeRepository extends JpaRepository<AdditiveType, Long>, JpaSpecificationExecutor<AdditiveType> {
    @Query(value = "select additive_type.id as id, additive_type.name as name, count(additive.id) as additiveQuantity, additive_type.status as status from additive_type left join additive on additive_type.id = additive.additive_type_id where additive_type.deleted = 0 group by additive_type.id", nativeQuery = true)
    Page<AdditiveTypeDTO> findAdditiveTypes(Pageable pageable);

    @Query(value = "select additive_type.id as id, additive_type.name as name, count(additive.id) as additiveQuantity, additive_type.status as status from additive_type left join additive on additive_type.id = additive.additive_type_id where additive_type.deleted = 0 and upper(additive_type.name) like :additType group by additive_type.id", nativeQuery = true)
    Page<AdditiveTypeDTO> searchAdditiveTypes(@Param("additType")String name, Pageable pageable);
}
