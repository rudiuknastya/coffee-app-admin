package project.specifications;

import org.springframework.data.jpa.domain.Specification;
import project.entity.City;

public interface CitySpecification {
    static Specification<City> byCityLike(String city){
        return (root, query, builder) ->
                builder.like(builder.upper(root.get("city")), "%"+city.toUpperCase()+"%");
    }
}
