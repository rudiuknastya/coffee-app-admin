package project.model.additiveModel;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class AdditiveRequest {
    private Long id;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    private String name;
    @NotNull(message = "Поле не може бути порожнім")
    @Digits(integer = 5, fraction = 0, message = "Розмір числа має бути не більше 5 символів")
    private BigDecimal price;
    @NotNull(message = "Поле не може бути порожнім")
    private Long additiveTypeId;
    private Boolean status;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getAdditiveTypeId() {
        return additiveTypeId;
    }

    public void setAdditiveTypeId(Long additiveTypeId) {
        this.additiveTypeId = additiveTypeId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
