package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Additive;
import project.model.additiveTypeModel.AdditiveTypeDTO;
import project.model.additiveTypeModel.AdditiveTypeNameDTO;
import project.entity.AdditiveType;
import project.mapper.AdditiveTypeMapper;
import project.repository.AdditiveRepository;
import project.repository.AdditiveTypeRepository;
import project.service.AdditiveTypeService;
import static project.specifications.AdditiveTypeSpecification.*;
import java.util.List;

@Service
public class AdditiveTypeServiceImpl implements AdditiveTypeService {
    private final AdditiveTypeRepository additiveTypeRepository;
    private final AdditiveRepository additiveRepository;

    public AdditiveTypeServiceImpl(AdditiveTypeRepository additiveTypeRepository, AdditiveRepository additiveRepository) {
        this.additiveTypeRepository = additiveTypeRepository;
        this.additiveRepository = additiveRepository;
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
    public Page<AdditiveTypeNameDTO> getAdditiveTypeNames(Pageable pageable, String name) {
        logger.info("getAdditiveTypeNames() - Finding additive type names for page "+pageable.getPageNumber()+" and search "+name);
        Page<AdditiveType> additiveTypes;
        if(name != null){
            additiveTypes = additiveTypeRepository.findAll(byDeleted().and(byNameLike(name)), pageable);
        } else {
            additiveTypes = additiveTypeRepository.findAll(byDeleted(), pageable);
        }
        List<AdditiveTypeNameDTO> additiveTypeNameDTOS = AdditiveTypeMapper.ADDITIVE_TYPE_MAPPER.additiveTypeToDTO(additiveTypes.getContent());
        Page<AdditiveTypeNameDTO> additiveTypeNameDTOPage = new PageImpl<>(additiveTypeNameDTOS, pageable, additiveTypes.getTotalElements());
        logger.info("getAdditiveTypeNames() - Additive type names were found");
        return additiveTypeNameDTOPage;
    }

    @Override
    public AdditiveTypeNameDTO getAdditiveTypeNameDTOById(Long id) {
        logger.info("getAdditiveTypeNameDTOById() - Finding additive type for additive type name dto by id "+ id);
        AdditiveType additiveType = additiveTypeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        AdditiveTypeNameDTO additiveTypeNameDTO = AdditiveTypeMapper.ADDITIVE_TYPE_MAPPER.additiveTypeToAdditiveTypeNameDTO(additiveType);
        logger.info("getAdditiveTypeNames() - Additive type name was found");
        return additiveTypeNameDTO;
    }

    @Override
    public List<AdditiveType> getAdditiveTypesByIds(Long[] adTypes) {
        logger.info("getAdditiveTypesByIds() - Finding additive types by ids");
        List<AdditiveType> additiveTypes = additiveTypeRepository.findAllById(List.of(adTypes));
        logger.info("getAdditiveTypesByIds() - Additive types by ids were found");
        return additiveTypes;
    }

    @Override
    public void updateAdditiveType(AdditiveType additiveType) {
        logger.info("updateAdditiveType() - Updating additive type");
        AdditiveType additiveTypeInDb = additiveTypeRepository.findById(additiveType.getId()).orElseThrow(EntityNotFoundException::new);
        additiveTypeInDb.setName(additiveType.getName());
        additiveTypeInDb.setStatus(additiveType.getStatus());
        additiveTypeRepository.save(additiveTypeInDb);
        logger.info("updateAdditiveType() - Additive type was updated");
    }

    @Override
    public void deleteAdditiveType(Long id) {
        logger.info("deleteAdditiveType() - Deleting additive type");
        AdditiveType additiveType = additiveTypeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        additiveType.setDeleted(true);
        List<Additive> additives = additiveRepository.findAdditivesForAdditiveType(id);
        for(Additive additive: additives){
            additive.setDeleted(true);
            additiveRepository.save(additive);
        }
        additiveTypeRepository.save(additiveType);
        logger.info("deleteAdditiveType() - Additive type was deleted");
    }
}
