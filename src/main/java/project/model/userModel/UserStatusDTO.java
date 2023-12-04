package project.model.userModel;

import project.entity.UserStatus;

public class UserStatusDTO {
    private UserStatus userStatus;
    private String name;

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
