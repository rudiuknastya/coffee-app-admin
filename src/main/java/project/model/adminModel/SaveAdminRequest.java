package project.model.adminModel;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import project.entity.Role;
import project.validators.confirmPassword.PasswordMatching;
import project.validators.emailValidation.EmailExist;
import project.validators.emailValidation.FieldEmailExist;

import java.time.LocalDate;
@PasswordMatching(
        newPassword = "newPassword",
        confirmNewPassword = "confirmNewPassword",
        message = "Паролі мають бути однаковими"
)
public class SaveAdminRequest {
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String firstName;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String lastName;
    @FieldEmailExist
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(max=200, message = "Розмір поля має бути не більше 200 символів")
    @Email(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-z]{2,3}", message = "Невірний формат email")
    private String email;
    @NotNull(message = "Поле не може бути порожнім")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @NotEmpty(message = "Поле не може бути порожнім ")
    private String city;
    @NotNull(message = "Поле не може бути порожнім")
    private Role role;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    @Pattern.List({
            @Pattern(regexp = ".{8,}", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8"),
            @Pattern(regexp = ".*\\d+.*", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8"),
            @Pattern(regexp = ".*[,./?]+.*", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8"),
            @Pattern(regexp = ".*[A-Z]+.*", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8")
    })
    private String newPassword;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String confirmNewPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
