package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.AwardDTO;

public interface AwardService {
    Page<AwardDTO> getAwards(Pageable pageable);
    Page<AwardDTO> searchAwards(String phone, Pageable pageable);
    void updateAward(Long userId, Long newProductId, Long oldProductId);
    void deleteAward(Long userId, Long productId);
}
