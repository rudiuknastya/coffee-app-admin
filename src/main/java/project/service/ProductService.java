package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductNameDTO;
import project.entity.Product;
import project.model.productModel.ProductResponse;

import java.util.List;

public interface ProductService {
    Page<ProductDTO> getProducts(Pageable pageable);
    Product saveProduct(Product product);
    Product getProductById(Long id);
    Product getProductWithAdditiveTypesById(Long id);
    ProductResponse getProductResponseById(Long id);
    Page<ProductDTO> searchProducts(String input, Long categoryId, Pageable pageable);
    List<Product> getProductsForCategory(Long id);
    Page<ProductNameDTO> getProductNameDTOS(Pageable pageable, String name);
    ProductNameDTO getProductNameDTO(Long id);
    Long getProductsCount();
}
