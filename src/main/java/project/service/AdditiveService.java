package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.dto.AdditiveDTO;
import project.entity.Additive;

import java.util.List;

public interface AdditiveService {
    Page<AdditiveDTO> getAllAdditives(Pageable pageable);
    Additive saveAdditive(Additive additive);
    Additive getAdditiveById(Long id);
    AdditiveDTO getAdditiveDTOById(Long id);
    Page<AdditiveDTO> searchAdditive(String input, Long additiveType, Pageable pageable);
}
