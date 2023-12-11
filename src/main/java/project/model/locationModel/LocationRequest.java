package project.model.locationModel;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import project.validators.phoneNumberValidation.LocationPhoneNumberExist;

@LocationPhoneNumberExist(
        id = "id",
        phoneNumber = "phoneNumber"
)
public class LocationRequest {
    private Long id;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String city;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=200, message = "Розмір поля має бути не більше 200 символів")
    private String address;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(max=13, message = "Розмір номеру має бути не більше 12 символів")
    @Pattern(regexp = "\\+?380(50)?(66)?(95)?(99)?(67)?(68)?(96)?(97)?(98)?(63)?(93)?(73)?[0-9]{7}", message = "Невірний формат номеру")
    private String phoneNumber;

    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String workingHours;
    @NotEmpty(message = "Поле не може бути порожнім")
    private String coordinates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
