package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.dto.AdditiveTypeDTO;
import project.dto.AdditiveTypeNameDTO;
import project.entity.AdditiveType;

import java.util.List;

public interface AdditiveTypeService {
    Page<AdditiveTypeDTO> getAdditiveTypes(Pageable pageable);
    Page<AdditiveTypeDTO> searchAdditiveTypes(String name, Pageable pageable);
    AdditiveType getAdditiveTypeById(Long id);
    AdditiveType saveAdditiveType(AdditiveType additiveType);
    List<AdditiveTypeNameDTO> getAdditiveTypeNames();
}
