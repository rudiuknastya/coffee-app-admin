package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.dto.ProductDTO;
import project.entity.Product;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Named("productToProductDTO")
    static List<ProductDTO> productToProductDTO(List<Product> products){
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
