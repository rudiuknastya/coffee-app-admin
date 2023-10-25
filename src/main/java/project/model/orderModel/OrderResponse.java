package project.model.orderModel;

import jakarta.validation.Valid;

public class OrderResponse {
    private Long id;
    private OrderStatusDTO status;
    private DeliveryResponse delivery;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatusDTO getStatus() {
        return status;
    }

    public void setStatus(OrderStatusDTO status) {
        this.status = status;
    }

    public DeliveryResponse getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryResponse delivery) {
        this.delivery = delivery;
    }
}
