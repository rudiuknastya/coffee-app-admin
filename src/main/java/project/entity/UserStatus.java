package project.entity;

public enum UserStatus {
    NEW("Новий"), ACTIVE("Активний"), BLOCKED("Заблокований");

    UserStatus(String statusName) {
        this.statusName = statusName;
    }

    private String statusName;

    public String getStatusName() {
        return statusName;
    }
}
