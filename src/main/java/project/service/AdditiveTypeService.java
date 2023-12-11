package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.additiveTypeModel.AdditiveTypeDTO;
import project.model.additiveTypeModel.AdditiveTypeNameDTO;
import project.entity.AdditiveType;
import project.model.additiveTypeModel.AdditiveTypeRequest;

import java.util.List;

public interface AdditiveTypeService {
    Page<AdditiveTypeDTO> getAdditiveTypes(Pageable pageable);
    Page<AdditiveTypeDTO> searchAdditiveTypes(String name, Pageable pageable);
    AdditiveType getAdditiveTypeById(Long id);
    AdditiveType saveAdditiveType(AdditiveType additiveType);
    Page<AdditiveTypeNameDTO> getAdditiveTypeNames(Pageable pageable, String name);
    AdditiveTypeNameDTO getAdditiveTypeNameDTOById(Long id);
    List<AdditiveType> getAdditiveTypesByIds(Long[] adTypes);
    void updateAdditiveType(AdditiveTypeRequest additiveTypeRequest);
    void deleteAdditiveType(Long id);
    void createAndSaveAdditiveType(AdditiveTypeRequest additiveTypeRequest);
}
