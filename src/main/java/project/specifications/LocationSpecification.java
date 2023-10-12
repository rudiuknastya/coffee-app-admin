package project.specifications;

import org.springframework.data.jpa.domain.Specification;
import project.entity.Location;

public interface LocationSpecification {
    static Specification<Location> byAddressLike(String addressPattern){
        return (root, query, builder) ->
                builder.like(builder.upper(root.get("address")), "%"+addressPattern.toUpperCase()+"%");
    }
    static Specification<Location> byCity(String city){
        return (root, query, builder) ->
                builder.equal(root.get("city"), city);
    }
    static Specification<Location> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }

}
