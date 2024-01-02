package project.specifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.entity.Additive;
import project.entity.AdditiveType;

import java.math.BigDecimal;

public interface AdditiveSpecification {
    static Specification<Additive> byName(String name){
        return (root, query, builder) ->
                builder.like(builder.upper(root.get("name")), "%"+name.toUpperCase()+"%");
    }
    static Specification<Additive> byAdditiveType(Long additiveType){
        return (root, query, builder) -> {
            Join<Additive, AdditiveType> additiveJoin = root.join("additiveType");
            return builder.equal(additiveJoin.get("id"), additiveType);
        };
    }
    static Specification<Additive> byPriceBetween(BigDecimal from, BigDecimal to){
        return (root, query, builder) ->
                builder.between(root.get("price"),from, to);
    }
    static Specification<Additive> byPriceGreaterThan(BigDecimal from){
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("price"),from);
    }
    static Specification<Additive> byPriceLessThan(BigDecimal to){
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("price"),to);
    }
    static Specification<Additive> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
}
