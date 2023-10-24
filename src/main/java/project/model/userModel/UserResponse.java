package project.model.userModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import project.entity.Language;
import project.entity.Role;
import project.entity.UserStatus;

import java.time.LocalDate;

public class UserResponse {
    private Long id;
    private String name;
    private String phoneNumber;

    private LocalDate birthDate;

    private LanguageDTO language;

    private UserStatusDTO status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LanguageDTO getLanguage() {
        return language;
    }

    public void setLanguage(LanguageDTO language) {
        this.language = language;
    }

    public UserStatusDTO getStatus() {
        return status;
    }

    public void setStatus(UserStatusDTO status) {
        this.status = status;
    }

}
