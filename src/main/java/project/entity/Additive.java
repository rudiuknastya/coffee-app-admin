package project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "additive")
public class Additive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(100)",nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    private Boolean deleted;
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "additive_type_id", referencedColumnName = "id")
    private AdditiveType additiveType;
    @ManyToMany(mappedBy = "additives",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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
