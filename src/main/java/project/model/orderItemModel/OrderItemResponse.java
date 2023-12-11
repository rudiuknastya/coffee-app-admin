package project.model.orderItemModel;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public class OrderItemResponse {
    private Long id;
    @NotNull(message = "Поле не може бути порожнім")
    @Digits(integer = 4, fraction = 0, message = "Розмір числа має бути не більше 4 символів")
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
