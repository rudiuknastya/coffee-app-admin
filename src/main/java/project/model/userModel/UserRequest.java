package project.model.userModel;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import project.entity.Language;
import project.entity.UserStatus;
import project.validators.emailValidation.UserEmailExist;
import project.validators.phoneNumberValidation.PhoneNumberExist;

import java.time.LocalDate;
@UserEmailExist(
        id = "id",
        email = "email"
)
@PhoneNumberExist(
        id = "id",
        phoneNumber = "phoneNumber"
)
public class UserRequest {
    private Long id;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String name;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(max=12, message = "Розмір поля має бути не більше 12 символів")
    @Pattern(regexp = "\\+?380(50)?(66)?(95)?(99)?(67)?(68)?(96)?(97)?(98)?(63)?(93)?(73)?[0-9]{0,7}", message = "Невірний формат номеру")
    private String phoneNumber;
    @NotNull(message = "Поле не може бути порожнім")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(max=200, message = "Розмір поля має бути не більше 200 символів")
    @Email(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-z]{2,3}", message = "Невірний формат email")
    private String email;

    private Language language;

    private UserStatus status;

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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
