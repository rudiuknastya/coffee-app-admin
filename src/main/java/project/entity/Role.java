package project.entity;

public enum Role {
    USER("Користувач"), ADMIN("Адмін"), MANAGER("Менеджер");

    Role(String roleName) {
        this.roleName = roleName;
    }
    private String roleName;

    public String getRoleName() {
        return roleName;
    }
}
