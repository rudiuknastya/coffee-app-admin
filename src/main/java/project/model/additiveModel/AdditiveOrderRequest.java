package project.model.additiveModel;

public class AdditiveOrderRequest {

    private Long orderItemId;
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
}
