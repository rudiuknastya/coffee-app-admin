package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.dto.AwardDTO;
import project.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query(value = "select award.user_id as userId, user.name as userName, user.phone_number as phoneNumber, award.product_id as productId, product.name as productName from user join award on user.id = award.user_id join product on award.product_id = product.id", nativeQuery = true)
    Page<AwardDTO> findUserAwards(Pageable pageable);

    @Query(value = "select award.user_id as userId, user.name as userName, user.phone_number as phoneNumber, award.product_id as productId, product.name as productName from user join award on user.id = award.user_id join product on award.product_id = product.id where upper(user.phone_number) like :phone", nativeQuery = true)
    Page<AwardDTO> findUserAwardsByUserPhoneNumber(@Param("phone")String phone, Pageable pageable);
    @Query(value = "SELECT u FROM User u INNER JOIN FETCH u.products p WHERE u.deleted=false AND u.id= :id")
    User findUserWithProductsById(@Param("id")Long id);

}
