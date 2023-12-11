package project.model.orderModel;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class DeliveryDTO {
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String name;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=13, message = "Розмір номеру має бути не більше 12 символів")
    @Pattern(regexp = "\\+?380(50)?(66)?(95)?(99)?(67)?(68)?(96)?(97)?(98)?(63)?(93)?(73)?[0-9]{7}", message = "Невірний формат номеру")
    private String phoneNumber;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String city;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String building;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String street;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String entrance;
    @NotNull(message = "Поле не може бути порожнім")
    @Digits(integer = 4, fraction = 0, message = "Розмір числа має бути не більше 4 символів")
    private Long apartment;
    @NotNull(message = "Поле не може бути порожнім")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;
    @NotNull(message = "Поле не може бути порожнім")
    @DateTimeFormat(pattern = "hh:mm:ss")
    private LocalTime deliveryTime;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public Long getApartment() {
        return apartment;
    }

    public void setApartment(Long apartment) {
        this.apartment = apartment;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
