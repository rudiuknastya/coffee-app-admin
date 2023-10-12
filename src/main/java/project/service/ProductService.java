package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.dto.ProductDTO;
import project.dto.ProductNameDTO;
import project.entity.Product;

import java.util.List;

public interface ProductService {
    Page<ProductDTO> getProducts(Pageable pageable);
    Product saveProduct(Product product);
    Product getProductById(Long id);
    Product getProductWithAdditiveTypesById(Long id);
    Page<ProductDTO> searchProducts(String input, Long categoryId, Pageable pageable);
    List<Product> getProductsForCategory(Long id);
    List<ProductNameDTO> getProductNames();
    ProductNameDTO getProductNameDTO(Long id);
}
