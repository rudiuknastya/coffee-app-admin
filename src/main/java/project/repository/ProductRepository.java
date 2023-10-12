package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query(value = "SELECT p FROM Product p INNER JOIN FETCH p.additiveTypes a WHERE p.deleted=false AND p.id= :id")
    Product findProductWithAdditiveTypesById(@Param("id")Long id);
    @Query(value = "SELECT product.id, product.name, product.price, product.description, product.image, product.deleted, product.status, product.category_id  FROM product INNER JOIN category ON product.category_id = category.id  WHERE product.category_id= :id AND product.deleted=false", nativeQuery = true)
    List<Product> findProductsForCategory(@Param("id")Long id);

}
