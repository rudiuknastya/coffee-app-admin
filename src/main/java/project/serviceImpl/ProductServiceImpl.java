package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.entity.AdditiveType;
import project.entity.Category;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductNameDTO;
import project.entity.Product;
import project.mapper.ProductMapper;
import project.model.productModel.ProductRequest;
import project.model.productModel.ProductResponse;
import project.repository.AdditiveTypeRepository;
import project.repository.CategoryRepository;
import project.repository.ProductRepository;
import project.service.ProductService;

import static project.specifications.ProductSpecification.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Value("${upload.path}")
    private String uploadPath;
    private Logger logger = LogManager.getLogger("serviceLogger");
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AdditiveTypeRepository additiveTypeRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, AdditiveTypeRepository additiveTypeRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.additiveTypeRepository = additiveTypeRepository;
    }
    @Override
    public Page<ProductDTO> getProducts(Pageable pageable) {
        logger.info("getProducts() - Finding products for page "+ pageable.getPageNumber());
        Page<Product> products = productRepository.findAll(byDeleted(), pageable);
        List<ProductDTO> productDTOS = ProductMapper.PRODUCT_MAPPER.productListToProductDTOList(products.getContent());
        Page<ProductDTO> productDTOPage = new PageImpl<>(productDTOS, pageable, products.getTotalElements());
        logger.info("getProducts() - Products were found");
        return productDTOPage;
    }

    @Override
    public void deleteProductById(Long id) {
        logger.info("deleteProductById() - Deleting product by id "+id);
        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Product was not found by id "+id));
        product.setDeleted(true);
        productRepository.save(product);
        logger.info("deleteProductById() - Product was deleted");
    }

    @Override
    public ProductResponse getProductResponseById(Long id) {
        logger.info("getProductResponseById() - Finding product for product response by id "+ id);
        Product product = productRepository.findProductWithAdditiveTypesById(id);
        ProductResponse productResponse = ProductMapper.PRODUCT_MAPPER.productToProductResponse(product);
        logger.info("getProductResponseById() - Product was found");
        return productResponse;
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
        logger.info("searchProducts() - Finding products for input "+ input+" and category id "+ categoryId);
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
        List<ProductDTO> productDTOS = ProductMapper.PRODUCT_MAPPER.productListToProductDTOList(products.getContent());
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

    @Override
    public Page<ProductNameDTO> getProductNameDTOS(Pageable pageable, String name) {
        logger.info("getProductNames() - Finding product names for page "+pageable.getPageNumber()+ " and search "+name);
        Page<Product> products;
        if(name == null) {
            products = productRepository.findAll(byDeleted(),pageable);
        } else {
            products = productRepository.findAll(byDeleted().and(byNameLike(name)),pageable);
        }
        List<ProductNameDTO> productNameDTOS = ProductMapper.PRODUCT_MAPPER.productListToProductNameDTOList(products.getContent());
        Page<ProductNameDTO> productNameDTOPage = new PageImpl<>(productNameDTOS, pageable, products.getTotalElements());
        logger.info("getProductNames() - Product names were found");
        return productNameDTOPage;
    }

    @Override
    public ProductNameDTO getProductNameDTO(Long id) {
        logger.info("getProductNameDTO() - Finding product for product name dto by id "+id);
        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Product was not found by id "+id));
        ProductNameDTO productNameDTO = ProductMapper.PRODUCT_MAPPER.productToProductNameDTO(product);
        logger.info("getProductNameDTO() - Product name was found");
        return productNameDTO;
    }

    @Override
    public Long getProductsCount() {
        logger.info("getProductsCount() - Finding products count");
        Long count = productRepository.findProductsCount();
        logger.info("getProductsCount() - Products count was found");
        return count;
    }

    @Override
    public void createAndSaveProduct(ProductRequest productRequest, Long[]adTypes, MultipartFile mainImage,String mainImageName) throws IOException {
        logger.info("createAndSaveProduct() - Creating and saving product");
        List<AdditiveType> additiveTypes = null;
        if(adTypes != null){
            additiveTypes = new ArrayList<>();
            for(int i = 0; i < adTypes.length; i++){
                AdditiveType additiveType = additiveTypeRepository.findById(adTypes[i]).orElseThrow(EntityNotFoundException::new);
                additiveTypes.add(additiveType);
            }
        }
        Product productToSave = ProductMapper.PRODUCT_MAPPER.productToProductRequest(productRequest);
        if(mainImage != null) {
            saveImage(mainImage, productToSave, mainImageName);
        }
        productToSave.setDeleted(false);
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(EntityNotFoundException::new);
        productToSave.setCategory(category);
        productToSave.setAdditiveTypes(additiveTypes);
        productRepository.save(productToSave);
        logger.info("createAndSaveProduct() - Product was created and saved");
    }

    @Override
    public void updateProduct(ProductRequest productRequest, Long[] adTypes, MultipartFile mainImage, String mainImageName) throws IOException {
        logger.info("updateProduct() - Updating product");
        Product productInDB = productRepository.findProductWithAdditiveTypesById(productRequest.getId());
        productInDB.getAdditiveTypes().clear();
        productRepository.save(productInDB);
        if(adTypes.length > 0) {
            List<AdditiveType> additiveTypes = additiveTypeRepository.findAllById(List.of(adTypes));
            productInDB.setAdditiveTypes(additiveTypes);
        }
        if(mainImage != null) {
            saveImage(mainImage, productInDB, mainImageName);
        }
        productInDB.setName(productRequest.getName());
        productInDB.setPrice(productRequest.getPrice());
        productInDB.setStatus(productRequest.getStatus());
        productInDB.setDescription(productRequest.getDescription());
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(EntityNotFoundException::new);
        productInDB.setCategory(category);
        productRepository.save(productInDB);
        logger.info("updateProduct() - Product was updated");
    }

    private void saveImage(MultipartFile image, Product product, String name) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }
        if (!image.getOriginalFilename().equals("") && name.equals("")) {
            String uuidFile = UUID.randomUUID().toString();
            String uniqueName = uuidFile + "." + image.getOriginalFilename();
            product.setImage(uniqueName);
            Path path = Paths.get(uploadPath + "/" + uniqueName);
            image.transferTo(new File(path.toUri()));

        } else if (image.getOriginalFilename().equals("") && name.equals("")) {
            File file = new File(uploadPath + "/" + product.getImage());
            file.delete();
            product.setImage(null);
        }else if(!image.getOriginalFilename().equals(name)&& !image.getOriginalFilename().equals("")){
            String uuidFile = UUID.randomUUID().toString();
            String uniqueName = uuidFile+"."+image.getOriginalFilename();
            product.setImage(uniqueName);
            Path path = Paths.get(uploadPath+"/"+uniqueName);
            image.transferTo(new File(path.toUri()));
            File file = new File(uploadPath+"/"+name);
            file.delete();
        } else if (!name.equals("")){
            product.setImage(name);
        }
    }
}
