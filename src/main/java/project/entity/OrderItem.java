package project.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long quantity;
    private Boolean deleted;
    @Column(nullable = false)
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
    @ManyToMany
    @JoinTable(
            name = "order_item_additive",
            joinColumns = { @JoinColumn(name = "order_item_id") },
            inverseJoinColumns = { @JoinColumn(name = "additive_id") }
    )
    private List<Additive> additives = new ArrayList<>();

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<Additive> getAdditives() {
        return additives;
    }

    public void setAdditives(List<Additive> additives) {
        this.additives = additives;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
