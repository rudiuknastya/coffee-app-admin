package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.OrderItem;
import project.model.additiveModel.AdditiveOrderRequest;
import project.model.orderModel.OrderAdditive;
import project.model.orderItemModel.OrderItemDTO;
import project.model.orderItemModel.OrderItemResponse;

public interface OrderItemService {
    Page<OrderItemDTO> getOrderItemDTOs(Pageable pageable, Long orderId);
    Page<OrderItemDTO> searchOrderItemDTOs(Pageable pageable, Long orderId, String name);
    Page<OrderAdditive> getOrderAdditives(Long orderItemId, Pageable pageable);
    Page<OrderAdditive> searchOrderAdditives(Long orderItemId, String name, Pageable pageable);
    OrderItemResponse getOrderItemResponseById(Long id);
    OrderItem getOrderItemWithAdditivesById(Long id);
    OrderItem getOrderItemById(Long id);
    OrderItem saveOrderItem(OrderItem orderItem);
    Long getOrderItemsCount(Long id);
    void deleteOrderItemById(Long id);
    void cancelOrder(Long id, String comment);
    void updateOrderItem(OrderItemResponse orderItemResponse);
    void updateOrderItemAdditive(AdditiveOrderRequest additiveOrderRequest, Long oldAdditiveId);
}
