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
import project.model.orderModel.OrderRequest;
import project.repository.OrderRepository;
import project.service.OrderService;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.OrderSpecification.*;

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
        Page<Order> orders = orderRepository.findAll(byDeleted(), pageable);
        List<OrderDTO> orderDTOS = OrderMapper.orderListToOrderDTOList(orders.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOS,pageable,orders.getTotalElements());
        logger.info("getOrders() - Orders were found");
        return orderDTOPage;
    }

    @Override
    public OrderRequest getOrderRequestById(Long id) {
        logger.info("getOrderRequestById() - Finding order for order request by id "+id);
        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        OrderRequest orderRequest = OrderMapper.orderToOrderRequest(order);
        logger.info("getOrderRequestById() - Order was found");
        return orderRequest;
    }

    @Override
    public Page<OrderDTO> searchOrders(Pageable pageable, String address, OrderStatus status, LocalDate date) {
        logger.info("searchOrders() - Finding orders for page "+ pageable.getPageNumber() + " for status "+ status+" for address "+address+" for date "+date);
        Page<Order> orders;
        if(status != null  &&  (address == null || address.equals("")) && date == null) {
            orders = orderRepository.findAll(where(byStatus(status).and(byDeleted())),pageable);
        } else if((address != null && !address.equals(""))  && status == null && date == null) {
            orders = orderRepository.findAll(where(byAddressLike(address).and(byDeleted())), pageable);
        } else if(date != null  &&  (address == null || address.equals("")) && status == null) {
            orders = orderRepository.findAll(where(byOrderDate(date).and(byDeleted())), pageable);
        } else if(date != null  &&  (address != null || !address.equals("")) && status == null){
            orders = orderRepository.findAll(where(byAddressLike(address).and(byOrderDate(date)).and(byDeleted())),pageable);
        } else if(date != null  &&  (address == null || address.equals("")) && status != null) {
            orders = orderRepository.findAll(where(byStatus(status).and(byOrderDate(date)).and(byDeleted())), pageable);
        } else if(status != null  &&  (address != null || !address.equals("")) && date == null) {
            orders = orderRepository.findAll(where(byStatus(status).and(byAddressLike(address)).and(byDeleted())),pageable);
        } else if((address != null && !address.equals("")) && status != null && date != null) {
            orders = orderRepository.findAll(where(byStatus(status).and(byAddressLike(address)).and(byOrderDate(date)).and(byDeleted())),pageable);
        } else {
            orders = orderRepository.findAll(byDeleted(),pageable);
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
        return null;
    }
}
