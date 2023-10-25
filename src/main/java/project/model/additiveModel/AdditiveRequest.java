package project.model.additiveModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class AdditiveRequest {
    private Long id;
    @NotEmpty(message = "Поле не може бути порожнім")
    private String name;
    @NotNull(message = "Поле не може бути порожнім")
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
