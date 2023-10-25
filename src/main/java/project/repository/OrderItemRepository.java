package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.OrderItem;
import project.model.orderModel.OrderAdditive;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
    @Query(value = "select additive.id as additiveId, additive_type.name as additiveTypeName, additive.name as additiveName from order_item inner join order_item_additive on order_item.id = order_item_additive.order_item_id inner join additive on additive.id = order_item_additive.additive_id inner join additive_type on additive.additive_type_id = additive_type.id where order_item_additive.deleted = 0 and order_item.id = :id ", nativeQuery = true)
    Page<OrderAdditive> findOrderAdditives(@Param("id")Long id, Pageable pageable);

    @Query(value = "select additive.id as additiveId, additive_type.name as additiveTypeName, additive.name as additiveName from order_item inner join order_item_additive on order_item.id = order_item_additive.order_item_id inner join additive on additive.id = order_item_additive.additive_id inner join additive_type on additive.additive_type_id = additive_type.id where order_item_additive.deleted = 0 and order_item.id = :id and upper(additive_type.name) like :adName", nativeQuery = true)
    Page<OrderAdditive> searchOrderAdditives(@Param("id")Long id, @Param("adName")String adName, Pageable pageable);
    @Query(value = "SELECT o FROM OrderItem o INNER JOIN FETCH o.additives a WHERE o.deleted=false AND o.id= :id")
    OrderItem findOrderItemWithAdditives(@Param("id")Long id);
}
