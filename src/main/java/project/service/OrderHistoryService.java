package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.OrderHistory;
import project.model.orderHistoryModel.OrderHistoryResponse;

import java.time.LocalDate;

public interface OrderHistoryService {
    OrderHistory saveOrderHistory(OrderHistory orderHistory);
    Page<OrderHistory> getOrderHistoriesByOrderId(Long id, Pageable pageable);
    OrderHistoryResponse getOrderHistoryResponseById(Long id);
    OrderHistory getOrderHistoryById(Long id);
    Page<OrderHistory> searchOrderHistories(Long id, String event, LocalDate date, Pageable pageable);

}
