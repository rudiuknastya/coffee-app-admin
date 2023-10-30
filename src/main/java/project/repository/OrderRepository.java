package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import project.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Query(value = "SELECT ifnull(avg(price),0) AS total_num\n" +
            "from (select STR_TO_DATE('01/01/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/02/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/03/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/04/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/05/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/06/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/07/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/08/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/09/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/10/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/11/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/12/2016', '%d/%m/%Y') as month_date) m\n" +
            "left join (select * from orders where not status = 'CANCELED') o\n" +
            "on month(m.month_date) = month(o.order_date)\n" +
            "GROUP BY month_date", nativeQuery = true)
    List<BigDecimal> findAvrgPricesInMonth();

    @Query(value = "SELECT ifnull(count(id),0) AS order_count\n" +
            "from (select STR_TO_DATE('01/01/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/02/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/03/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/04/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/05/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/06/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/07/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/08/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/09/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/10/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/11/2016', '%d/%m/%Y') as month_date union \n" +
            " select STR_TO_DATE('01/12/2016', '%d/%m/%Y') as month_date) m\n" +
            "left join orders o\n" +
            "on month(m.month_date) = month(o.order_date)\n" +
            "GROUP BY month_date", nativeQuery = true)
    List<Long> findOrdersCountInMonth();
    @Query(value = "select count(id) from orders where not status = 'CANCELED'", nativeQuery = true)
    Long findOrdersCount();
}
