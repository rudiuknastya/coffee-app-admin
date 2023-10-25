package project.specifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.entity.Order;
import project.entity.OrderItem;
import project.entity.Product;

public interface OrderItemSpecification {
    static Specification<OrderItem> byProductNameLike(String name){
        return (root, query, builder) -> {
            Join<OrderItem, Product> productJoin = root.join("product");
            return builder.like(builder.upper(productJoin.get("name")), "%" + name.toUpperCase() + "%");
        };
    }
    static Specification<OrderItem> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
    static Specification<OrderItem> byOrder(Long orderId){
        return (root, query, builder) -> {
            Join<OrderItem, Order> orderJoin = root.join("order");
            return builder.equal(orderJoin.get("id"), orderId);
        };
    }
}
