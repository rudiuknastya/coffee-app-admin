package project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import project.model.orderModel.OrderAdditive;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemAdditiveId implements Serializable {
    @Column(name = "order_item_id")
    private Long orderItemId;
    @Column(name = "additive_id")
    private Long additiveId;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getAdditiveId() {
        return additiveId;
    }

    public void setAdditiveId(Long additiveId) {
        this.additiveId = additiveId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemId, additiveId);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj ==  null || getClass() != obj.getClass()){
            return false;
        }
        OrderItemAdditiveId that = (OrderItemAdditiveId) obj;
        return Objects.equals(orderItemId, that.orderItemId) &&
                Objects.equals(additiveId, that.additiveId);
    }
}
