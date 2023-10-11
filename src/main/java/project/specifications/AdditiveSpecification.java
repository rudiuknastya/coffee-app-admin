package project.specifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.entity.Additive;
import project.entity.AdditiveType;
import project.entity.Location;

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
    static Specification<Additive> byPrice(Integer price){
        return (root, query, builder) ->
                builder.equal(root.get("price"), price);
    }
    static Specification<Additive> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
}
