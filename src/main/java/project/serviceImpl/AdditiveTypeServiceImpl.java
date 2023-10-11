package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.AdditiveTypeDTO;
import project.dto.AdditiveTypeNameDTO;
import project.dto.CategoryDTO;
import project.entity.AdditiveType;
import project.mapper.AdditiveTypeMapper;
import project.repository.AdditiveTypeRepository;
import project.service.AdditiveTypeService;

import java.util.List;

@Service
public class AdditiveTypeServiceImpl implements AdditiveTypeService {
    private final AdditiveTypeRepository additiveTypeRepository;

    public AdditiveTypeServiceImpl(AdditiveTypeRepository additiveTypeRepository) {
        this.additiveTypeRepository = additiveTypeRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<AdditiveTypeDTO> getAdditiveTypes(Pageable pageable) {
        logger.info("getAdditiveTypes() - Finding all additive types for page "+ pageable.getPageNumber());
        Page<AdditiveTypeDTO> additiveTypeDTOS = additiveTypeRepository.findAdditiveTypes(pageable);
        logger.info("getAllCategories() - All additive types were found");
        return additiveTypeDTOS;
    }

    @Override
    public AdditiveType getAdditiveTypeById(Long id) {
        logger.info("getAdditiveTypeById() - Finding additive type for id "+ id);
        AdditiveType additiveType = additiveTypeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getAdditiveTypeById() - Additive type was found");
        return additiveType;
    }

    @Override
    public AdditiveType saveAdditiveType(AdditiveType additiveType) {
        logger.info("saveAdditiveType() - Saving additive type");
        AdditiveType additiveType1 = additiveTypeRepository.save(additiveType);
        logger.info("saveAdditiveType() - Additive type was saved");
        return additiveType1;
    }

    @Override
    public Page<AdditiveTypeDTO> searchAdditiveTypes(String name, Pageable pageable) {
        logger.info("searchAdditiveTypes() - Finding additive type by name like "+name);
        String n = "%"+name.toUpperCase()+"%";
        Page<AdditiveTypeDTO> additiveTypeDTOS = additiveTypeRepository.searchAdditiveTypes(n,pageable);
        logger.info("searchAdditiveTypes() - Additive type were found");
        return additiveTypeDTOS;
    }

    @Override
    public List<AdditiveTypeNameDTO> getAdditiveTypeNames() {
        logger.info("getAdditiveTypeNames() - Finding additive type names");
        List<AdditiveType> additiveTypes = additiveTypeRepository.findByDeletedNot(true);
        List<AdditiveTypeNameDTO> additiveTypeNameDTOS = AdditiveTypeMapper.ADDITIVE_TYPE_MAPPER.additiveTypeToDTO(additiveTypes);
        logger.info("getAdditiveTypeNames() - Additive type names were found");
        return additiveTypeNameDTOS;
    }
}
