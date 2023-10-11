package project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "additive")
public class Additive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "Поле не може бути порожнім")
    @Column(nullable = false)
    private Integer price;
    private Boolean deleted;
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "additive_type_id", referencedColumnName = "id")
    private AdditiveType additiveType;

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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public AdditiveType getAdditiveType() {
        return additiveType;
    }

    public void setAdditiveType(AdditiveType additiveType) {
        this.additiveType = additiveType;
    }
}
