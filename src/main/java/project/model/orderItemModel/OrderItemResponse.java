package project.model.orderItemModel;

import jakarta.validation.constraints.NotNull;

public class OrderItemResponse {
    private Long id;
    @NotNull(message = "Поле не може бути порожнім")
    private Long quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
