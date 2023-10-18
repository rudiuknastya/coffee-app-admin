package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.model.AwardDTO;
import project.repository.UserRepository;
import project.service.AwardService;
@Service
public class AwardServiceImpl implements AwardService {
    private final UserRepository userRepository;

    public AwardServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<AwardDTO> getAwards(Pageable pageable) {
        logger.info("getAwards() - Finding awards for page "+ pageable.getPageNumber());
        Page<AwardDTO> awardDTOS = userRepository.findUserAwards(pageable);
        logger.info("getAwards() - Awards were found");
        return awardDTOS;
    }

    @Override
    public Page<AwardDTO> searchAwards(String phone, Pageable pageable) {
        logger.info("searchAwards() - Finding awards for user with phone number "+ phone);
        Page<AwardDTO> awardDTOS = userRepository.findUserAwardsByUserPhoneNumber("%"+phone.toUpperCase()+"%",pageable);
        logger.info("getAwards() - Awards were found");
        return awardDTOS;
    }
}
