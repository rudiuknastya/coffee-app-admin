package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.AdditiveType;
import project.model.additiveModel.AdditiveDTO;
import project.entity.Additive;
import project.model.additiveModel.AdditiveOrderResponse;
import project.model.additiveModel.AdditiveOrderSelect;
import project.model.additiveModel.AdditiveRequest;

import java.util.List;

public interface AdditiveService {
    Page<AdditiveDTO> getAllAdditives(Pageable pageable);
    Additive saveAdditive(Additive additive);
    Additive getAdditiveById(Long id);
    Page<AdditiveDTO> searchAdditive(String input, Long additiveType, Pageable pageable);
    List<Additive> getAdditivesForAdditiveType(Long id);
    AdditiveRequest getAdditiveResponseById(Long id);
    AdditiveOrderResponse getAdditiveOrderResponseById(Long id);
    Page<AdditiveOrderSelect> getAdditivesForAdditiveTypeForOrder(Long id, Pageable pageable);
}
