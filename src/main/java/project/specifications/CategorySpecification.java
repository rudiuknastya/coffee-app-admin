package project.specifications;

import org.springframework.data.jpa.domain.Specification;
import project.entity.Category;

public interface CategorySpecification {
    static Specification<Category> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
    static Specification<Category> byNameLike(String name){
        return (root, query, builder) ->
                builder.like(builder.upper(root.get("name")), "%"+name.toUpperCase()+"%");
    }
}
