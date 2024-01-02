package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import project.entity.Order;
import project.entity.OrderHistory;
import project.mapper.OrderHistoryMapper;
import project.model.orderHistoryModel.OrderHistoryDTO;
import project.model.orderHistoryModel.OrderHistoryResponse;
import project.repository.OrderHistoryRepository;
import project.service.OrderHistoryService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.OrderHistorySpecification.*;
@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private Logger logger = LogManager.getLogger("serviceLogger");
    private final OrderHistoryRepository orderHistoryRepository;
    public OrderHistoryServiceImpl(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @Override
    public Page<OrderHistoryResponse> getOrderHistoriesByOrderId(Long id, Pageable pageable) {
        logger.info("getOrderHistoriesByOrderId() - Finding order history by order id "+id+" and page "+pageable.getPageNumber());
        Page<OrderHistory> orderHistories = orderHistoryRepository.findAll(byOrderId(id), pageable);
        List<OrderHistoryResponse> orderHistoryResponses = OrderHistoryMapper.ORDER_HISTORY_MAPPER.orderHistoryToOrderHistoryResponse(orderHistories.getContent());
        Page<OrderHistoryResponse> orderHistoryResponsePage = new PageImpl<>(orderHistoryResponses,pageable,orderHistories.getTotalElements());
        logger.info("saveOrderHistory() - Order histories were found");
        return orderHistoryResponsePage;
    }

    @Override
    public OrderHistoryDTO getOrderHistoryResponseById(Long id) {
        logger.info("getOrderHistoryResponseById() - Finding order history for order history response by id "+id);
        OrderHistory orderHistory = orderHistoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Order history was not found by id "+id));
        OrderHistoryDTO orderHistoryDTO = OrderHistoryMapper.ORDER_HISTORY_MAPPER.orderHistoryToOrderHistoryDTO(orderHistory);
        logger.info("getOrderHistoryResponseById() - Order history was found");
        return orderHistoryDTO;
    }

    @Override
    public void updateOrderHistory(OrderHistoryDTO orderHistoryDTO) {
        logger.info("updateOrderHistory() - Updating order history by id "+orderHistoryDTO.getId());
        OrderHistory orderHistory = orderHistoryRepository.findById(orderHistoryDTO.getId()).orElseThrow(()->new EntityNotFoundException("Order history was not found by id "+orderHistoryDTO.getId()));
        orderHistory.setComment(orderHistoryDTO.getComment());
        orderHistoryRepository.save(orderHistory);
        logger.info("updateOrderHistory() - Order history was updated");
    }

    @Override
    public Page<OrderHistoryResponse> searchOrderHistories(Long id, String event, LocalDate date, Pageable pageable) {
        logger.info("searchOrderHistories() - Searching order history by order id "+id+" and search "+event+" and date "+date);
        Page<OrderHistory> orderHistories = filterOrderHistories(id, event, date, pageable);
        List<OrderHistoryResponse> orderHistoryResponses = OrderHistoryMapper.ORDER_HISTORY_MAPPER.orderHistoryToOrderHistoryResponse(orderHistories.getContent());
        Page<OrderHistoryResponse> orderHistoryResponsePage = new PageImpl<>(orderHistoryResponses,pageable,orderHistories.getTotalElements());
        logger.info("searchOrderHistories() - Order histories were found");
        return orderHistoryResponsePage;
    }
    private Page<OrderHistory> filterOrderHistories(Long id, String event, LocalDate date, Pageable pageable){
        Specification<OrderHistory> specification = Specification.where(byOrderId(id));
        if(event != null){
            specification = specification.and(byEventLike(event));
        }
        if(date != null){
            specification = specification.and(byDate(date));
        }
        return orderHistoryRepository.findAll(specification,pageable);
    }

}
