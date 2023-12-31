package project.model.additiveModel;

import java.math.BigDecimal;

public class AdditiveDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String additiveTypeName;
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

    public String getAdditiveTypeName() {
        return additiveTypeName;
    }

    public void setAdditiveTypeName(String additiveTypeName) {
        this.additiveTypeName = additiveTypeName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
