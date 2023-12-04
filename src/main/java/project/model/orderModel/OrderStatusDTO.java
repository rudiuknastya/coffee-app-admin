package project.model.orderModel;

import project.entity.OrderStatus;

public class OrderStatusDTO {
    private OrderStatus orderStatus;
    private String name;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
