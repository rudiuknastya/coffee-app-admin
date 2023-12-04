package project.specifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.entity.Order;
import project.entity.OrderHistory;

import java.time.LocalDate;

public interface OrderHistorySpecification {
    static Specification<OrderHistory> byOrderId(Long id){
        return (root, query, builder) -> {
            Join<OrderHistory, Order> orderJoin = root.join("order");
            return builder.equal(orderJoin.get("id"), id);
        };
    }
    static Specification<OrderHistory> byEventLike(String event){
        return (root, query, builder) ->
                builder.like(builder.upper(root.get("event")), "%"+event.toUpperCase()+"%");
    }
    static Specification<OrderHistory> byDate(LocalDate date){
        return (root, query, builder) ->
                builder.equal(root.get("eventDate"), date);
    }
}
