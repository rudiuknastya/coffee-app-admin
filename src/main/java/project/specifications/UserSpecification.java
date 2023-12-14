package project.specifications;

import org.springframework.data.jpa.domain.Specification;
import project.entity.User;
import project.entity.UserStatus;

import java.time.LocalDate;

public interface UserSpecification {
    static Specification<User> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
    static Specification<User> byPhoneNumberLike(String phoneNumber){
        return (root, query, builder) ->
                builder.like(builder.upper(root.get("phoneNumber")), "%"+phoneNumber.toUpperCase()+"%");
    }
    static Specification<User> byStatus(UserStatus status){
        return (root, query, builder) ->
                builder.equal(root.get("status"), status);
    }
    static Specification<User> byBirthDate(LocalDate date){
        return (root, query, builder) ->
                builder.equal(root.get("birthDate"), date);
    }
}
