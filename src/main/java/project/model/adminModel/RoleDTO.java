package project.model.adminModel;

import project.entity.Role;

public class RoleDTO {
    private Role role;
    private String name;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
