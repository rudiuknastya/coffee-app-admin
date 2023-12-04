package project.entity;

public enum OrderStatus {
    ORDERED("Замовлено"), CALL("Передзвонити"), READY("Готово"),DELIVERED("Доставлено"), CANCELED("Відмінено");

    OrderStatus(String statusName) {
        this.statusName = statusName;
    }

    private String statusName;

    public String getStatusName() {
        return statusName;
    }
}
