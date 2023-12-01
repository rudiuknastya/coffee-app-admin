package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.OrderStatus;
import project.model.orderModel.OrderDTO;
import project.entity.Order;
import project.mapper.OrderMapper;
import project.model.orderModel.OrderResponse;
import project.repository.OrderRepository;
import project.service.OrderService;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.OrderSpecification.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<OrderDTO> getOrders(Pageable pageable) {
        logger.info("getOrders() - Finding orders for page "+ pageable.getPageNumber());
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderDTO> orderDTOS = OrderMapper.orderListToOrderDTOList(orders.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOS,pageable,orders.getTotalElements());
        logger.info("getOrders() - Orders were found");
        return orderDTOPage;
    }

    @Override
    public OrderResponse getOrderResponseById(Long id) {
        logger.info("getOrderResponseById() - Finding order for order response by id "+id);
        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        OrderResponse orderResponse = OrderMapper.orderToOrderRequest(order);
        logger.info("getOrderResponseById() - Order was found");
        return orderResponse;
    }

    @Override
    public Page<OrderDTO> searchOrders(Pageable pageable, String address, OrderStatus status, LocalDate date) {
        logger.info("searchOrders() - Finding orders for page "+ pageable.getPageNumber() + " for status "+ status+" for address "+address+" for date "+date);
        Page<Order> orders;
        if(status != null  &&  (address == null || address.equals("")) && date == null) {
            orders = orderRepository.findAll(byStatus(status),pageable);
        } else if((address != null && !address.equals(""))  && status == null && date == null) {
            orders = orderRepository.findAll(byAddressLike(address), pageable);
        } else if(date != null  &&  (address == null || address.equals("")) && status == null) {
            orders = orderRepository.findAll(byOrderDate(date), pageable);
        } else if(date != null  &&  (address != null || !address.equals("")) && status == null){
            orders = orderRepository.findAll(where(byAddressLike(address).and(byOrderDate(date))),pageable);
        } else if(date != null  &&  (address == null || address.equals("")) && status != null) {
            orders = orderRepository.findAll(where(byStatus(status).and(byOrderDate(date))), pageable);
        } else if(status != null  &&  (address != null || !address.equals("")) && date == null) {
            orders = orderRepository.findAll(where(byStatus(status).and(byAddressLike(address))),pageable);
        } else if((address != null && !address.equals("")) && status != null && date != null) {
            orders = orderRepository.findAll(where(byStatus(status).and(byAddressLike(address)).and(byOrderDate(date))),pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }
        List<OrderDTO> orderDTOS = OrderMapper.orderListToOrderDTOList(orders.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOS,pageable,orders.getTotalElements());
        logger.info("searchOrders() - Orders were found");
        return orderDTOPage;
    }

    @Override
    public Order gerOrderById(Long id) {
        logger.info("gerOrderById() - Finding order by id "+id);
        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("gerOrderById() - Order was found");
        return order;
    }

    @Override
    public Order saveOrder(Order order) {
        logger.info("saveOrder() - Saving order");
        Order order1 = orderRepository.save(order);
        logger.info("saveOrder() - Order was saved");
        return order1;
    }

    @Override
    public List<BigDecimal> getOrderAvrgPricesInMonth() {
        logger.info("getOrderAvrgPricesInMonth() - Finding order average prices in month");
        List<BigDecimal> prices = orderRepository.findAvrgPricesInMonth();
        logger.info("getOrderAvrgPricesInMonth() - Order average prices in month were found");
        return prices;
    }

    @Override
    public Long getOrdersCount() {
        logger.info("getOrdersCount() - Finding orders count");
        Long count = orderRepository.findOrdersCount();
        logger.info("getOrdersCount() - Orders count was found");
        return count;
    }

    @Override
    public List<Long> getOrdersCountInMonth() {
        logger.info("getOrdersCountInMonth() - Finding orders count in month");
        List<Long> counts = orderRepository.findOrdersCountInMonth();
        logger.info("getOrdersCount() - Orders count was found");
        return counts;
    }
}
