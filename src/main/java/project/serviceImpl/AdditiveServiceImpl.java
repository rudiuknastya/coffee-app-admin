package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.AdditiveDTO;
import project.entity.Additive;
import project.mapper.AdditiveMapper;
import project.repository.AdditiveRepository;
import project.service.AdditiveService;

import java.util.List;

import static project.specifications.AdditiveSpecification.*;

@Service
public class AdditiveServiceImpl implements AdditiveService {
    private final AdditiveRepository additiveRepository;

    public AdditiveServiceImpl(AdditiveRepository additiveRepository) {
        this.additiveRepository = additiveRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<AdditiveDTO> getAllAdditives(Pageable pageable) {
        logger.info("getAllAdditives() - Finding all additives for page "+ pageable.getPageNumber());
        Page<Additive> additives = additiveRepository.findAll(byDeleted(), pageable);
        List<AdditiveDTO> additiveDTOS = AdditiveMapper.ADDITIVE_MAPPER.additiveListToDTO(additives.getContent());
        Page<AdditiveDTO> additiveDTOPage = new PageImpl<>(additiveDTOS,pageable,additives.getTotalElements());
        logger.info("getAllAdditives() - All additives were found");
        return additiveDTOPage;
    }

    @Override
    public Additive saveAdditive(Additive additive) {
        logger.info("saveAdditive() - Saving additive");
        Additive additive1 = additiveRepository.save(additive);
        logger.info("saveAdditive() - Additive was saved");
        return additive1;
    }

    @Override
    public Additive getAdditiveById(Long id) {
        logger.info("getAdditiveById() - Finding additive by id "+id);
        Additive additive = additiveRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getAdditiveById() - Additive was found");
        return additive;
    }

    @Override
    public AdditiveDTO getAdditiveDTOById(Long id) {
        logger.info("getAdditiveDTOById() - Finding additive for dto by id "+id);
        Additive additive = additiveRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        AdditiveDTO additiveDTO = AdditiveMapper.ADDITIVE_MAPPER.additiveToDTO(additive);
        logger.info("getAdditiveDTOById() - Additive was found");
        return additiveDTO;
    }

    @Override
    public Page<AdditiveDTO> searchAdditive(String input, Long additiveType, Pageable pageable) {
        logger.info("searchAdditive() - Finding additive for input "+input+" additive type "+additiveType);
        Page<Additive> additives;
        if((additiveType != null && !additiveType.equals(0L)) &&  (input == null || input.equals(""))){
            additives = additiveRepository.findAll(byDeleted().and(byAdditiveType(additiveType)), pageable);
        } else if((input != null && !input.equals(""))  && (additiveType == null || additiveType.equals(0L))) {
            try {
                Integer price = Integer.parseInt(input);
                additives = additiveRepository.findAll(byDeleted().and(byPrice(price)),pageable);
            } catch (NumberFormatException e) {
                additives = additiveRepository.findAll(byDeleted().and(byName(input)),pageable);
            }
        } else if((input != null && !input.equals(""))  && (additiveType != null && !additiveType.equals(0L))){
            try {
                Integer price = Integer.parseInt(input);
                additives = additiveRepository.findAll(byDeleted().and(byPrice(price).and(byAdditiveType(additiveType))),pageable);
            } catch (NumberFormatException e) {
                additives = additiveRepository.findAll(byDeleted().and(byName(input).and(byAdditiveType(additiveType))),pageable);
            }
        } else {
            additives = additiveRepository.findAll(byDeleted(),pageable);
        }
        List<AdditiveDTO> additiveDTOS = AdditiveMapper.ADDITIVE_MAPPER.additiveListToDTO(additives.getContent());
        Page<AdditiveDTO> additiveDTOPage = new PageImpl<>(additiveDTOS,pageable,additives.getTotalElements());
        logger.info("searchAdditive() - Additives were found");
        return additiveDTOPage;
    }

    @Override
    public List<Additive> getAdditivesForAdditiveType(Long id) {
        logger.info("getAdditivesForAdditiveType() - Finding additives for additive type with id "+id);
        List<Additive> additives = additiveRepository.findAdditivesForAdditiveType(id);
        logger.info("getAdditivesForAdditiveType() - Additives were found");
        return additives;
    }
}
