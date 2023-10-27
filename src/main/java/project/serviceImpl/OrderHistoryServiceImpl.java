package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.OrderHistory;
import project.mapper.OrderHistoryMapper;
import project.model.orderHistoryModel.OrderHistoryResponse;
import project.repository.OrderHistoryRepository;
import project.service.OrderHistoryService;

import java.time.LocalDate;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.OrderHistorySpecification.*;
@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryServiceImpl(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public OrderHistory saveOrderHistory(OrderHistory orderHistory) {
        logger.info("saveOrderHistory() - Saving order history");
        OrderHistory orderHistory1 = orderHistoryRepository.save(orderHistory);
        logger.info("saveOrderHistory() - Order history was saved");
        return orderHistory1;
    }

    @Override
    public Page<OrderHistory> getOrderHistoriesByOrderId(Long id, Pageable pageable) {
        logger.info("getOrderHistoriesByOrderId() - Finding order history by order id "+id+" and page "+pageable.getPageNumber());
        Page<OrderHistory> orderHistories = orderHistoryRepository.findAll(byOrderId(id), pageable);
        logger.info("saveOrderHistory() - Order histories were found");
        return orderHistories;
    }

    @Override
    public OrderHistoryResponse getOrderHistoryResponseById(Long id) {
        logger.info("getOrderHistoryResponseById() - Finding order history for order history response by id "+id);
        OrderHistory orderHistory = orderHistoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        OrderHistoryResponse orderHistoryResponse = OrderHistoryMapper.ORDER_HISTORY_MAPPER.orderHistoryToOrderHistoryResponse(orderHistory);
        logger.info("getOrderHistoryResponseById() - Order history was found");
        return orderHistoryResponse;
    }

    @Override
    public OrderHistory getOrderHistoryById(Long id) {
        logger.info("getOrderHistoryById() - Finding order history by id "+id);
        OrderHistory orderHistory = orderHistoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getOrderHistoryById() - Order history was found");
        return orderHistory;
    }

    @Override
    public Page<OrderHistory> searchOrderHistories(Long id, String event, LocalDate date, Pageable pageable) {
        logger.info("searchOrderHistories() - Searching order history by order id "+id+" and search "+event+" and date "+date);
        Page<OrderHistory> orderHistories;
        if(date != null &&  (event == null || event.equals(""))) {
            System.out.println("if 1");
            orderHistories = orderHistoryRepository.findAll(where(byOrderId(id).and(byDate(date))),pageable);
        } else if((event != null && !event.equals(""))  && date == null){
            System.out.println("if 2");
            orderHistories = orderHistoryRepository.findAll(where(byEventLike(event).and(byOrderId(id))),pageable);
        } else if((event != null && !event.equals("")) && date != null) {
            System.out.println("if 3");
            orderHistories = orderHistoryRepository.findAll(where(byDate(date).and(byEventLike(event).and(byOrderId(id)))),pageable);
        } else {
            System.out.println("if 4");
            orderHistories = orderHistoryRepository.findAll(byOrderId(id),pageable);
        }
        logger.info("searchOrderHistories() - Order histories were found");
        return orderHistories;
    }
}
