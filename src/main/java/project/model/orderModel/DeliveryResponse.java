package project.model.orderModel;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class DeliveryResponse {
    @NotEmpty(message = "Поле не може бути порожнім")
    private String name;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(min=4, max=15, message = "Розмір поля має бути не менше 4 та не більше 15 символів")
    @Pattern(regexp = "^\\+?[1-9][0-9]{4,15}$", message = "Невірний формат номеру")
    @Column(name = "phone_number", columnDefinition="VARCHAR(20) NOT NULL UNIQUE")
    private String phoneNumber;
    @NotEmpty(message = "Поле не може бути порожнім")
    private String city;
    @NotEmpty(message = "Поле не може бути порожнім")
    private String building;
    @NotEmpty(message = "Поле не може бути порожнім")
    private String street;
    @NotEmpty(message = "Поле не може бути порожнім")
    private String entrance;
    @NotNull(message = "Поле не може бути порожнім")
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
