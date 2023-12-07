package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.model.AwardDTO;
import project.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query(value = "select award.user_id as userId, user.name as userName, user.phone_number as phoneNumber, award.product_id as productId, product.name as productName from user join award on user.id = award.user_id join product on award.product_id = product.id", nativeQuery = true)
    Page<AwardDTO> findUserAwards(Pageable pageable);

    @Query(value = "select award.user_id as userId, user.name as userName, user.phone_number as phoneNumber, award.product_id as productId, product.name as productName from user join award on user.id = award.user_id join product on award.product_id = product.id where upper(user.phone_number) like :phone", nativeQuery = true)
    Page<AwardDTO> findUserAwardsByUserPhoneNumber(@Param("phone")String phone, Pageable pageable);
    @Query(value = "SELECT u FROM User u INNER JOIN FETCH u.products p WHERE u.deleted=false AND u.id= :id")
    User findUserWithProductsById(@Param("id")Long id);
    Optional<User> findByPhoneNumber(String phoneNumber);
    @Query(value = "select t.s *100/count(id) as perc from user cross join (select count(id) as s from user where language = 'UKR')t GROUP BY s", nativeQuery = true)
    Long findPercentageOfUkr();
    @Query(value = "select t.s *100/count(id) as perc from user cross join (select count(id) as s from user where language = 'ENG')t GROUP BY s", nativeQuery = true)
    Long findPercentageOfEng();
    @Query(value = "select t.s *100/count(id) as perc from user cross join (select count(id) as s from user where language = 'SPA')t GROUP BY s", nativeQuery = true)
    Long findPercentageOfSpa();
    @Query(value = "select count(id) from user where deleted = 0", nativeQuery = true)
    Long findUsersCount();
    Optional<User> findByEmail(String email);

}
