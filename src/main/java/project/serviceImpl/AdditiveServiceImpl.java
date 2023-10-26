package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.model.additiveModel.AdditiveDTO;
import project.entity.Additive;
import project.mapper.AdditiveMapper;
import project.model.additiveModel.AdditiveOrderResponse;
import project.model.additiveModel.AdditiveOrderSelect;
import project.model.additiveModel.AdditiveRequest;
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
        List<AdditiveDTO> additiveDTOS = AdditiveMapper.ADDITIVE_MAPPER.additiveListToDTOList(additives.getContent());
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
    public AdditiveRequest getAdditiveRequestById(Long id) {
        logger.info("getAdditiveRequestById() - Finding additive for additive request by id "+id);
        Additive additive = additiveRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        AdditiveRequest additiveRequest = AdditiveMapper.ADDITIVE_MAPPER.additiveTOAdditiveRequest(additive);
        System.out.println(additiveRequest.getAdditiveTypeId());
        logger.info("getAdditiveRequestById() - Additive was found");
        return additiveRequest;
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
        List<AdditiveDTO> additiveDTOS = AdditiveMapper.ADDITIVE_MAPPER.additiveListToDTOList(additives.getContent());
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

    @Override
    public AdditiveOrderResponse getAdditiveOrderResponseById(Long id) {
        logger.info("getAdditiveOrderResponseById() - Finding additive for additive order response by id "+id);
        Additive additive = additiveRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        AdditiveOrderResponse additiveOrderResponse = AdditiveMapper.ADDITIVE_MAPPER.additiveToAdditiveOrderResponse(additive);
        logger.info("getAdditiveOrderResponseById() - Additive was found");
        return additiveOrderResponse;
    }

    @Override
    public Page<AdditiveOrderSelect> getAdditivesForAdditiveTypeForOrder(Long id, Pageable pageable) {
        logger.info("getAdditivesForAdditiveTypeForOrder() - Finding additives for order for additive type with id "+id);
        Page<Additive> additives = additiveRepository.findAdditivesForAdditiveType(id,pageable);
        List<AdditiveOrderSelect> additiveOrderSelects = AdditiveMapper.ADDITIVE_MAPPER.additiveListToAdditiveOrderSelectList(additives.getContent());
        Page<AdditiveOrderSelect> additiveOrderSelectPage = new PageImpl<>(additiveOrderSelects,pageable,additives.getTotalElements());
        return additiveOrderSelectPage;
    }
}
