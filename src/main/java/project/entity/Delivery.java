package project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=15, message = "Розмір поля має бути не більше 15 символів")
    @Column(name = "phone_number", columnDefinition="VARCHAR(20) NOT NULL UNIQUE")
    private String phoneNumber;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String building;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String entrance;
    private Long apartment;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Payment payment;
    @Column(name = "remainder_from")
    private Long remainderFrom;
    @Column(name = "delivery_date", columnDefinition="DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;
    @Column(name="delivery_time", columnDefinition="TIME NOT NULl")
    @DateTimeFormat(pattern = "hh:mm:ss")
    private LocalTime deliveryTime;
    private Boolean deleted;
    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Long getRemainderFrom() {
        return remainderFrom;
    }

    public void setRemainderFrom(Long remainderFrom) {
        this.remainderFrom = remainderFrom;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
