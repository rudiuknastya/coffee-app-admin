package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.Order;
import project.entity.OrderStatus;
import project.model.orderModel.OrderDTO;
import project.model.orderModel.OrderResponse;

import java.time.LocalDate;

public interface OrderService {
    Page<OrderDTO> getOrders(Pageable pageable);
    OrderResponse getOrderResponseById(Long id);
    Page<OrderDTO> searchOrders(Pageable pageable, String address, OrderStatus status, LocalDate date);
    Order gerOrderById(Long id);
    Order saveOrder(Order order);

}
