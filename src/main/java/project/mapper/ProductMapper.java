package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductNameDTO;
import project.entity.Product;
import project.model.productModel.ProductRequest;
import project.model.productModel.ProductResponse;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper PRODUCT_MAPPER = Mappers.getMapper(ProductMapper.class);
    List<ProductNameDTO> productListToProductNameDTOList(List<Product> products);
    ProductNameDTO productToProductNameDTO(Product product);
    Product productToProductRequest(ProductRequest productRequest);
    ProductResponse productToProductResponse(Product product);
    List<ProductDTO> productListToProductDTOList(List<Product> products);
    @Mapping(target = "categoryName", source = "category.name")
    ProductDTO productToProductDTO(Product product);
}
