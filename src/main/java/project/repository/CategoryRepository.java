package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.dto.CategoryDTO;
import project.entity.Category;


public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    @Query(value = "select category.id as id, category.name as name, count(product.id) as goodsQuantity, category.status as status from category left join product on category.id = product.category_id where category.deleted = 0 group by category.id", nativeQuery = true)
    Page<CategoryDTO> findCategories(Pageable pageable);

    @Query(value = "select category.id as id, category.name as name, count(product.id) as goodsQuantity, category.status as status from category left join product on category.id = product.category_id where category.deleted = 0 and upper(category.name) like :catName group by category.id", nativeQuery = true)
    Page<CategoryDTO> searchCategories(@Param("catName")String name, Pageable pageable);

}
