package project.model.additiveModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AdditiveRequest {
    private Long id;
    @NotEmpty(message = "Поле не може бути порожнім")
    private String name;
    @NotNull(message = "Поле не може бути порожнім")
    private Long price;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
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
