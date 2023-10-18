package project.model.productModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import project.entity.AdditiveType;

import java.util.List;

public class ProductRequest {
    private Long id;
    @NotEmpty(message = "Поле не може бути порожнім")
    private String name;
    @NotNull(message = "Поле не може бути порожнім")
    private Integer price;
    @NotEmpty(message = "Поле не може бути порожнім")
    private String description;
    private String image;
    private Boolean status;
    @NotNull(message = "Поле не може бути порожнім")
    private Long categoryId;
    private List<AdditiveType> additiveTypes;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

     public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<AdditiveType> getAdditiveTypes() {
        return additiveTypes;
    }

    public void setAdditiveTypes(List<AdditiveType> additiveTypes) {
        this.additiveTypes = additiveTypes;
    }
}
