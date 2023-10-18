package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductNameDTO;
import project.entity.Product;
import project.model.productModel.ProductRequest;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper PRODUCT_MAPPER = Mappers.getMapper(ProductMapper.class);
    List<ProductNameDTO> productListToProductNameDTOList(List<Product> products);
    ProductNameDTO productToProductNameDTO(Product product);
    @Named("productToProductRequest")
    static ProductRequest productToProductRequest(Product product){
        if(product == null){
            return null;
        }
        ProductRequest productRequest = new ProductRequest();
        productRequest.setId(product.getId());
        productRequest.setName(product.getName());
        productRequest.setPrice(product.getPrice());
        productRequest.setImage(product.getImage());
        productRequest.setDescription(product.getDescription());
        productRequest.setStatus(product.getStatus());
        productRequest.setCategoryId(product.getCategory().getId());
        productRequest.setAdditiveTypes(product.getAdditiveTypes());
        return productRequest;
    }
    @Named("productToProductDTO")
    static List<ProductDTO> productToProductDTO(List<Product> products){
        if(products == null){
            return null;
        }
        List<ProductDTO> productDTOS = new ArrayList<>(products.size());
        for(Product product: products ){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());
            productDTO.setStatus(product.getStatus());
            productDTO.setCategoryName(product.getCategory().getName());
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }
}
