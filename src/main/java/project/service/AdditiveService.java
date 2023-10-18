package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.additiveModel.AdditiveDTO;
import project.entity.Additive;
import project.model.additiveModel.AdditiveRequest;

import java.util.List;

public interface AdditiveService {
    Page<AdditiveDTO> getAllAdditives(Pageable pageable);
    Additive saveAdditive(Additive additive);
    Additive getAdditiveById(Long id);
    Page<AdditiveDTO> searchAdditive(String input, Long additiveType, Pageable pageable);
    List<Additive> getAdditivesForAdditiveType(Long id);
    AdditiveRequest getAdditiveRequestById(Long id);
}
