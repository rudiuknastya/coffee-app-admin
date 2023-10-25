package project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item_additive")
public class OrderItemAdditive {
    @EmbeddedId
    private OrderItemAdditiveId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderItemId")
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("additiveId")
    @JoinColumn(name = "additive_id")
    private Additive additive;

    private Boolean deleted;
    public OrderItemAdditiveId getId() {
        return id;
    }

    public void setId(OrderItemAdditiveId id) {
        this.id = id;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Additive getAdditive() {
        return additive;
    }

    public void setAdditive(Additive additive) {
        this.additive = additive;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
