package project.model.orderModel;

import jakarta.validation.Valid;
import project.entity.OrderStatus;

public class OrderRequest {
    private Long id;
    private OrderStatus status;
    private DeliveryResponse delivery;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public DeliveryResponse getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryResponse delivery) {
        this.delivery = delivery;
    }
}
