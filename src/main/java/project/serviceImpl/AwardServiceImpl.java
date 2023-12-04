package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Product;
import project.entity.User;
import project.model.AwardDTO;
import project.repository.ProductRepository;
import project.repository.UserRepository;
import project.service.AwardService;
@Service
public class AwardServiceImpl implements AwardService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public AwardServiceImpl(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
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

    @Override
    public void updateAward(Long userId, Long newProductId, Long oldProductId) {
        logger.info("updateAward() - Updating award");
        User user = userRepository.findUserWithProductsById(userId);
        int i = 0;
        for(Product product: user.getProducts()){
            if(product.getId().equals(oldProductId)){
                Product product1 = productRepository.findById(newProductId).orElseThrow(EntityNotFoundException::new);
                user.getProducts().set(i, product1);
            }
            i++;
        }
        userRepository.save(user);
        logger.info("updateAward() - Award was updated");
    }

    @Override
    public void deleteAward(Long userId, Long productId) {
        logger.info("deleteAward() - Deleting award");
        User user = userRepository.findUserWithProductsById(userId);
        int count = 0;
        int productToRemove = 0;
        int i = 0;
        for(Product product: user.getProducts()){
            if(product.getId().equals(productId) && count <= 0){
                productToRemove = i;
                count++;
            }
            i++;
        }
        user.getProducts().remove(productToRemove);
        userRepository.save(user);
        logger.info("deleteAward() - Award was deleted");
    }
}
