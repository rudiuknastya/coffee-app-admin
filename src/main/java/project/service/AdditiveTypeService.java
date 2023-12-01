package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.additiveTypeModel.AdditiveTypeDTO;
import project.model.additiveTypeModel.AdditiveTypeNameDTO;
import project.entity.AdditiveType;

import java.util.List;

public interface AdditiveTypeService {
    Page<AdditiveTypeDTO> getAdditiveTypes(Pageable pageable);
    Page<AdditiveTypeDTO> searchAdditiveTypes(String name, Pageable pageable);
    AdditiveType getAdditiveTypeById(Long id);
    AdditiveType saveAdditiveType(AdditiveType additiveType);
    Page<AdditiveTypeNameDTO> getAdditiveTypeNames(Pageable pageable, String name);
    AdditiveTypeNameDTO getAdditiveTypeNameDTOById(Long id);
    List<AdditiveType> getAdditiveTypesByIds(Long[] adTypes);
}
