package project.specifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.entity.Category;
import project.entity.Product;

public interface ProductSpecification {
    static Specification<Product> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
    static Specification<Product> byNameLike(String name){
        return (root, query, builder) ->
                builder.like(builder.upper(root.get("name")), "%"+name.toUpperCase()+"%");
    }
    static Specification<Product> byPrice(Integer price){
        return (root, query, builder) ->
                builder.equal(root.get("price"), price);
    }
    static Specification<Product> byCategory(Long categoryId){
        return (root, query, builder) -> {
            Join<Product, Category> productJoin = root.join("category");
            return builder.equal(productJoin.get("id"), categoryId);
        };
    }
}
