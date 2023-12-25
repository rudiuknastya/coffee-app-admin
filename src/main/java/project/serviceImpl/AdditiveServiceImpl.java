package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.AdditiveType;
import project.model.additiveModel.AdditiveDTO;
import project.entity.Additive;
import project.mapper.AdditiveMapper;
import project.model.additiveModel.AdditiveOrderResponse;
import project.model.additiveModel.AdditiveOrderSelect;
import project.model.additiveModel.AdditiveRequest;
import project.repository.AdditiveRepository;
import project.repository.AdditiveTypeRepository;
import project.service.AdditiveService;

import java.math.BigDecimal;
import java.util.List;

import static project.specifications.AdditiveSpecification.*;

@Service
public class AdditiveServiceImpl implements AdditiveService {
    private Logger logger = LogManager.getLogger("serviceLogger");
    private final AdditiveRepository additiveRepository;
    private final AdditiveTypeRepository additiveTypeRepository;

    public AdditiveServiceImpl(AdditiveRepository additiveRepository, AdditiveTypeRepository additiveTypeRepository) {
        this.additiveRepository = additiveRepository;
        this.additiveTypeRepository = additiveTypeRepository;
    }
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
    public void createAdditive(AdditiveRequest additiveRequest) {
        logger.info("createAdditive() - Creating additive");
        Additive additive = AdditiveMapper.ADDITIVE_MAPPER.additiveRequestToAdditive(additiveRequest);
        AdditiveType additiveType = additiveTypeRepository.findById(additiveRequest.getAdditiveTypeId()).orElseThrow(()-> new EntityNotFoundException("Additive type was not found by id "+additiveRequest.getAdditiveTypeId()));
        additive.setAdditiveType(additiveType);
        additiveRepository.save(additive);
        logger.info("createAdditive() - Additive was created");
    }

    @Override
    public void updateAdditive(AdditiveRequest additiveRequest) {
        logger.info("updateAdditive() - Updating additive");
        Additive additiveInDB = additiveRepository.findById(additiveRequest.getId()).orElseThrow(()-> new EntityNotFoundException("Additive was not found by id "+additiveRequest.getId()));
        AdditiveType additiveType = additiveTypeRepository.findById(additiveRequest.getAdditiveTypeId()).orElseThrow(()-> new EntityNotFoundException("Additive type was not found by id "+additiveRequest.getAdditiveTypeId()));
        AdditiveMapper.ADDITIVE_MAPPER.setAdditiveRequest(additiveInDB,additiveRequest,additiveType);
        additiveRepository.save(additiveInDB);
        logger.info("updateAdditive() - Additive was updated");
    }

    @Override
    public Additive getAdditiveById(Long id) {
        logger.info("getAdditiveById() - Finding additive by id "+id);
        Additive additive = additiveRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Additive was not found by id "+id));
        logger.info("getAdditiveById() - Additive was found");
        return additive;
    }

    @Override
    public AdditiveRequest getAdditiveResponseById(Long id) {
        logger.info("getAdditiveResponseById() - Finding additive for additive response by id "+id);
        Additive additive = additiveRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Additive was not found by id "+ id));
        AdditiveRequest additiveRequest = AdditiveMapper.ADDITIVE_MAPPER.additiveTOAdditiveRequest(additive);
        logger.info("getAdditiveResponseById() - Additive was found");
        return additiveRequest;
    }
    @Override
    public Page<AdditiveDTO> searchAdditive(String input, Long additiveType, BigDecimal from, BigDecimal to, Pageable pageable) {
        logger.info("searchAdditive() - Finding additive for input "+input+" additive type "+additiveType);
        Page<Additive> additives = filterAdditives(input,additiveType,from,to,pageable);
        List<AdditiveDTO> additiveDTOS = AdditiveMapper.ADDITIVE_MAPPER.additiveListToDTOList(additives.getContent());
        Page<AdditiveDTO> additiveDTOPage = new PageImpl<>(additiveDTOS,pageable,additives.getTotalElements());
        logger.info("searchAdditive() - Additives were found");
        return additiveDTOPage;
    }
    private Page<Additive> filterAdditives(String name, Long additiveType, BigDecimal from, BigDecimal to, Pageable pageable){
        if(name == null  &&  additiveType == null && from != null && to != null){
            return additiveRepository.findAll(byDeleted().and(byPriceBetween(from,to)), pageable);
        } else if (name == null  &&  additiveType == null && from == null && to != null){
            return additiveRepository.findAll(byDeleted().and(byPriceLessThan(to)), pageable);
        } else if (name == null  &&  additiveType == null && from != null && to == null) {
            return additiveRepository.findAll(byDeleted().and(byPriceGreaterThan(from)), pageable);
        } else if(name == null  &&  additiveType != null && from != null && to != null){
            return additiveRepository.findAll(byDeleted().and(byPriceBetween(from,to)).and(byAdditiveType(additiveType)), pageable);
        } else if (name == null  &&  additiveType != null && from == null && to != null){
            return additiveRepository.findAll(byDeleted().and(byPriceLessThan(to)).and(byAdditiveType(additiveType)), pageable);
        } else if (name == null  &&  additiveType != null && from != null && to == null) {
            return additiveRepository.findAll(byDeleted().and(byPriceGreaterThan(from)).and(byAdditiveType(additiveType)), pageable);
        } else if (name != null  &&  additiveType == null && from != null && to != null) {
            return additiveRepository.findAll(byDeleted().and(byPriceBetween(from,to)).and(byName(name)), pageable);
        } else if (name != null  &&  additiveType == null && from == null && to != null){
            return additiveRepository.findAll(byDeleted().and(byPriceLessThan(to)).and(byName(name)), pageable);
        } else if (name != null  &&  additiveType == null && from != null && to == null) {
            return additiveRepository.findAll(byDeleted().and(byPriceGreaterThan(from)).and(byName(name)), pageable);
        }else if (name != null  &&  additiveType != null && from != null && to != null) {
            return additiveRepository.findAll(byDeleted().and(byPriceBetween(from,to)).and(byName(name)).and(byAdditiveType(additiveType)), pageable);
        } else if (name != null  &&  additiveType != null && from == null && to != null){
            return additiveRepository.findAll(byDeleted().and(byPriceLessThan(to)).and(byName(name)).and(byAdditiveType(additiveType)), pageable);
        } else if (name != null  &&  additiveType != null && from != null && to == null) {
            return additiveRepository.findAll(byDeleted().and(byPriceGreaterThan(from)).and(byName(name)).and(byAdditiveType(additiveType)), pageable);
        } else if (name != null  &&  additiveType != null && from == null && to == null) {
            return additiveRepository.findAll(byDeleted().and(byName(name)).and(byAdditiveType(additiveType)), pageable);
        } else if (name != null  &&  additiveType == null && from == null && to == null) {
            return additiveRepository.findAll(byDeleted().and(byName(name)), pageable);
        } else {
            return additiveRepository.findAll(byDeleted(),pageable);
        }
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
        Additive additive = additiveRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Additive was not found by id "+id));
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
        logger.info("getAdditivesForAdditiveTypeForOrder() - Additives for order were found");
        return additiveOrderSelectPage;
    }

    @Override
    public void deleteAdditiveById(Long id) {
        logger.info("deleteAdditiveById() - Deleting additive by id "+id);
        Additive additiveInDB = additiveRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Additive was not found by id "+id));
        additiveInDB.setDeleted(true);
        additiveRepository.save(additiveInDB);
        logger.info("deleteAdditiveById() - Additives was deleted");
    }
}
