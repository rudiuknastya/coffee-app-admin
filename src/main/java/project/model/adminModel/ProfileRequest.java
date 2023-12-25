package project.model.adminModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import project.validators.confirmPassword.PasswordMatching;
import project.validators.emailValidation.EmailExist;
import project.validators.newPasswordValidator.NewPasswordValid;
import project.validators.notEmptyPasswords.NotEmptyOldPassword;

import java.time.LocalDate;
@EmailExist(
        id = "id",
        email = "email"
)
@NotEmptyOldPassword(
        newPassword = "newPassword",
        oldPassword = "oldPassword"
)
@PasswordMatching(
        newPassword = "newPassword",
        confirmNewPassword = "confirmNewPassword"
)
public class ProfileRequest {
    private Long id;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=25, message = "Розмір поля має бути не більше 25 символів")
    private String firstName;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=25, message = "Розмір поля має бути не більше 25 символів")
    private String lastName;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    @Email(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-z]{2,3}", message = "Невірний формат email")
    private String email;
    @NotNull(message = "Поле не може бути порожнім")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String city;
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String oldPassword;
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    @NewPasswordValid
    private String newPassword;
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String confirmNewPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
