package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.Order;
import project.entity.OrderHistory;
import project.model.orderHistoryModel.OrderHistoryDTO;
import project.model.orderHistoryModel.OrderHistoryResponse;

import java.time.LocalDate;

public interface OrderHistoryService {
    OrderHistory saveOrderHistory(OrderHistory orderHistory);
    void createAndSaveOrderHistory(String event, Order order);
    void createAndSaveOrderHistory(String event, Order order,String comment);
    Page<OrderHistoryResponse> getOrderHistoriesByOrderId(Long id, Pageable pageable);
    OrderHistoryDTO getOrderHistoryResponseById(Long id);
    OrderHistory getOrderHistoryById(Long id);
    Page<OrderHistoryResponse> searchOrderHistories(Long id, String event, LocalDate date, Pageable pageable);

}
