package project.specifications;

import org.springframework.data.jpa.domain.Specification;
import project.entity.Admin;
import project.entity.Role;

public interface AdminSpecification {
    static Specification<Admin> byRole(Role role){
        return (root, query, builder) ->
                builder.equal(root.get("role"), role);
    }
    static Specification<Admin> byLastNameLike(String lastName){
        return (root, query, builder) ->
                builder.like(builder.upper(root.get("lastName")), "%"+lastName.toUpperCase()+"%");
    }
    static Specification<Admin> byEmailLike(String email){
        return (root, query, builder) ->
                builder.like(builder.upper(root.get("email")), "%"+email.toUpperCase()+"%");
    }
    static Specification<Admin> byEmailNot(String email){
        return (root, query, builder) ->
                builder.notEqual(root.get("email"),email);
    }
}
