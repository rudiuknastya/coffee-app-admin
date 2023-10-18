package project.specifications;

import org.springframework.data.jpa.domain.Specification;
import project.entity.Additive;
import project.entity.AdditiveType;
import project.entity.City;

public interface AdditiveTypeSpecification {
    static Specification<AdditiveType> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
    static Specification<AdditiveType> byNameLike(String name){
        return (root, query, builder) ->
                builder.like(builder.upper(root.get("name")), "%"+name.toUpperCase()+"%");
    }
}
