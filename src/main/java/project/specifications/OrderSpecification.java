package project.specifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.entity.Location;
import project.entity.Order;
import project.entity.OrderStatus;

import java.time.LocalDate;

public interface OrderSpecification {
    static Specification<Order> byStatus(OrderStatus status){
        return (root, query, builder) ->
                builder.equal(root.get("status"), status);
    }
    static Specification<Order> byOrderDate(LocalDate date){
        return (root, query, builder) ->
                builder.equal(root.get("orderDate"), date);
    }
    static Specification<Order> byAddressLike(String address){
        return (root, query, builder) -> {
            Join<Order, Location> locationJoin = root.join("location");
            return builder.like(builder.upper(locationJoin.get("address")), "%" + address.toUpperCase() + "%");
        };
    }
}
