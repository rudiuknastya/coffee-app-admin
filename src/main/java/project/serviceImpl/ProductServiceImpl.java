package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.ProductDTO;
import project.entity.Product;
import project.mapper.ProductMapper;
import project.repository.ProductRepository;
import project.service.ProductService;

import static project.specifications.ProductSpecification.*;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<ProductDTO> getProducts(Pageable pageable) {
        logger.info("getProducts() - Finding products for page "+ pageable.getPageNumber());
        Page<Product> products = productRepository.findAll(byDeleted(), pageable);
        System.out.println(products.getContent().get(0).getCategory().getName());
        List<ProductDTO> productDTOS = ProductMapper.productToProductDTO(products.getContent());
        System.out.println(productDTOS.get(0).getCategoryName());
        Page<ProductDTO> productDTOPage = new PageImpl<>(productDTOS, pageable, products.getTotalElements());
        logger.info("getProducts() - Products were found");
        return productDTOPage;
    }

    @Override
    public Product saveProduct(Product product) {
        logger.info("saveProduct() - Saving product");
        Product product1 = productRepository.save(product);
        logger.info("saveProduct() - Product was saved");
        return product1;
    }

    @Override
    public Product getProductById(Long id) {
        logger.info("getProductById() - Finding product by id "+ id);
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getProductById() - Product was found");
        return product;
    }

    @Override
    public Product getProductWithAdditiveTypesById(Long id) {
        logger.info("getProductWithAdditiveTypesById() - Finding product with additive types by id "+ id);
        Product product = productRepository.findProductWithAdditiveTypesById(id);
        logger.info("getProductWithAdditiveTypesById() - Product was found");
        return product;
    }

    @Override
    public Page<ProductDTO> searchProducts(String input, Long categoryId, Pageable pageable) {
        logger.info("searchProducts() - Finding products for input "+ input+" and category id"+ categoryId);
        Page<Product> products;
        if((categoryId != null && !categoryId.equals(0L)) &&  (input == null || input.equals(""))){
            products = productRepository.findAll(byDeleted().and(byCategory(categoryId)), pageable);
        } else if((input != null && !input.equals(""))  && (categoryId == null || categoryId.equals(0L))) {
            try {
                Integer price = Integer.parseInt(input);
                products = productRepository.findAll(byDeleted().and(byPrice(price)),pageable);
            } catch (NumberFormatException e) {
                products = productRepository.findAll(byDeleted().and(byNameLike(input)),pageable);
            }
        } else if((input != null && !input.equals(""))  && (categoryId != null && !categoryId.equals(0L))){
            try {
                Integer price = Integer.parseInt(input);
                products = productRepository.findAll(byDeleted().and(byPrice(price).and(byCategory(categoryId))),pageable);
            } catch (NumberFormatException e) {
                products = productRepository.findAll(byDeleted().and(byNameLike(input).and(byCategory(categoryId))),pageable);
            }
        } else {
            products = productRepository.findAll(byDeleted(),pageable);
        }
        List<ProductDTO> productDTOS = ProductMapper.productToProductDTO(products.getContent());
        Page<ProductDTO> productDTOPage = new PageImpl<>(productDTOS, pageable, products.getTotalElements());
        logger.info("searchProducts() - Products were found");
        return productDTOPage;
    }

    @Override
    public List<Product> getProductsForCategory(Long id) {
        logger.info("getProductsForCategory() - Finding products for category with id "+ id);
        List<Product> products = productRepository.findProductsForCategory(id);
        logger.info("getProductsForCategory() - Products were found");
        return products;
    }
}
