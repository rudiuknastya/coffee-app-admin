package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Additive;
import project.entity.Order;
import project.entity.OrderHistory;
import project.mapper.OrderItemMapper;
import project.model.additiveModel.AdditiveOrderRequest;
import project.model.orderModel.OrderAdditive;
import project.model.orderItemModel.OrderItemDTO;
import project.entity.OrderItem;
import project.model.orderItemModel.OrderItemResponse;
import project.repository.AdditiveRepository;
import project.repository.OrderHistoryRepository;
import project.repository.OrderItemRepository;
import project.service.OrderItemService;
import static project.specifications.OrderItemSpecification.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final AdditiveRepository additiveRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, OrderHistoryRepository orderHistoryRepository, AdditiveRepository additiveRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.additiveRepository = additiveRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<OrderItemDTO> getOrderItemDTOs(Pageable pageable, Long orderId) {
        logger.info("getOrderItemDTOs() - Finding order items for page "+ pageable.getPageNumber());
        Page<OrderItem> orderItems = orderItemRepository.findAll(byDeleted().and(byOrder(orderId)),pageable);
        List<OrderItemDTO> orderItemDTOS = OrderItemMapper.orderListToOrderItemDTOList(orderItems.getContent());
        Page<OrderItemDTO> orderItemDTOPage = new PageImpl<>(orderItemDTOS, pageable, orderItems.getTotalElements());
        logger.info("getOrderItemDTOs() - Order items were found");
        return orderItemDTOPage;
    }

    @Override
    public Page<OrderItemDTO> searchOrderItemDTOs(Pageable pageable, Long orderId, String name) {
        logger.info("searchOrderItemDTOs() - Finding order items for page "+ pageable.getPageNumber()+ " and search "+name);
        Page<OrderItem> orderItems = orderItemRepository.findAll(byProductNameLike(name).and(byDeleted()).and(byOrder(orderId)), pageable);
        List<OrderItemDTO> orderItemDTOS = OrderItemMapper.orderListToOrderItemDTOList(orderItems.getContent());
        Page<OrderItemDTO> orderItemDTOPage = new PageImpl<>(orderItemDTOS, pageable, orderItems.getTotalElements());
        logger.info("searchOrderItemDTOs() - Order items were found");
        return orderItemDTOPage;
    }

    @Override
    public Page<OrderAdditive> getOrderAdditives(Long orderItemId, Pageable pageable) {
        logger.info("getOrderAdditives() - Finding order additives for page "+pageable.getPageNumber());
        Page<OrderAdditive> orderAdditives = orderItemRepository.findOrderAdditives(orderItemId, pageable);
        logger.info("getOrderAdditives() - Order additives were found");
        return orderAdditives;
    }

    @Override
    public OrderItemResponse getOrderItemResponseById(Long id) {
        logger.info("getOrderItemResponseById() - Finding order item for order item response by id "+id);
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        OrderItemResponse orderItemRequest = OrderItemMapper.ORDER_ITEM_MAPPER.orderToOrderItemRequest(orderItem);
        logger.info("getOrderItemResponseById() - Order item was found");
        return orderItemRequest;
    }

    @Override
    public Page<OrderAdditive> searchOrderAdditives(Long orderItemId, String name, Pageable pageable) {
        logger.info("searchOrderAdditives() - Finding order additives for page "+pageable.getPageNumber()+ " and search "+name);
        String n = "%"+name.toUpperCase()+"%";
        Page<OrderAdditive> orderAdditives = orderItemRepository.searchOrderAdditives(orderItemId, n, pageable);
        logger.info("searchOrderAdditives() - Order additives were found");
        return orderAdditives;
    }

    @Override
    public OrderItem getOrderItemWithAdditivesById(Long id) {
        logger.info("getOrderItemRequestById() - Finding order item with additives by id "+id);
        OrderItem orderItem = orderItemRepository.findOrderItemWithAdditives(id);
        logger.info("getOrderItemRequestById() - Order item was found");
        return orderItem;
    }

    @Override
    public OrderItem getOrderItemById(Long id) {
        logger.info("getOrderItemById() - Finding order item by id "+id);
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getOrderItemById() - Order item was found");
        return orderItem;
    }

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        logger.info("saveOrderItem() - Saving order item");
        OrderItem orderItem1 = orderItemRepository.save(orderItem);
        logger.info("saveOrderItem() - Order item was saved");
        return orderItem1;
    }

    @Override
    public Long getOrderItemsCount(Long id) {
        logger.info("getOrderItemsCount() - Finding order items count");
        Long count = orderItemRepository.getOrderItemsCount(id);
        logger.info("getOrderItemsCount() - Order items count was found");
        return count;
    }

    @Override
    public void updateOrderItem(OrderItemResponse orderItemResponse) {
        logger.info("updateOrderItem() - Updating order item");
        OrderItem orderItem = orderItemRepository.findById(orderItemResponse.getId()).orElseThrow(EntityNotFoundException::new);
        if(!orderItem.getQuantity().equals(orderItemResponse.getQuantity())){
            String s = "Змінено кількість товарів у замовленні з "+orderItem.getQuantity()+" на "+orderItemResponse.getQuantity();
            createAndSaveOrderHistory(s,orderItem.getOrder());
        }
        BigDecimal p = orderItem.getPrice();
        p = p.divide(new BigDecimal(orderItem.getQuantity()));
        p = p.multiply(new BigDecimal(orderItemResponse.getQuantity()));
        BigDecimal op = orderItem.getOrder().getPrice();
        op = op.subtract(orderItem.getPrice());
        orderItem.setPrice(p);
        orderItem.setQuantity(orderItemResponse.getQuantity());
        op = op.add(orderItem.getPrice());
        orderItem.getOrder().setPrice(op);
        orderItemRepository.save(orderItem);
        logger.info("updateOrderItem() - Order item was updated");
    }

    @Override
    public void updateOrderItemAdditive(AdditiveOrderRequest additiveOrderRequest, Long oldAdditiveId) {
        logger.info("updateOrderItemAdditive() - Updating order item additive");
        Additive newAdditive = additiveRepository.findById(additiveOrderRequest.getAdditiveId()).orElseThrow(EntityNotFoundException::new);
        OrderItem orderItem = orderItemRepository.findOrderItemWithAdditives(additiveOrderRequest.getOrderItemId());
        int i = 0;
        for(Additive additive: orderItem.getAdditives()){
            if(additive.getId().equals(oldAdditiveId)){
                orderItem.getAdditives().set(i,newAdditive);
                BigDecimal p = orderItem.getPrice();
                BigDecimal op = orderItem.getOrder().getPrice();
                op = op.subtract(orderItem.getPrice());
                p = p.subtract(additive.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
                p = p.add(newAdditive.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
                orderItem.setPrice(p);
                op = op.add(orderItem.getPrice());
                orderItem.getOrder().setPrice(op);
                if(!additive.getName().equals(newAdditive.getName()) ) {
                    String s = "Змінено додаток для товару " + orderItem.getProduct().getName() + " з " + additive.getName() + " на " + newAdditive.getName();
                    createAndSaveOrderHistory(s,orderItem.getOrder());
                }
            }
            i++;
        }
        orderItemRepository.save(orderItem);
        logger.info("updateOrderItemAdditive() - Order item additive was updated");
    }

    private void createAndSaveOrderHistory(String event, Order order) {
        OrderHistory orderHistory = new OrderHistory(event, LocalDate.now(), LocalTime.now(),order);
        orderHistoryRepository.save(orderHistory);
    }
}
