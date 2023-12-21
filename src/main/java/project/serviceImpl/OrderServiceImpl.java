package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.OrderHistory;
import project.entity.OrderStatus;
import project.model.orderModel.DeliveryDTO;
import project.model.orderModel.OrderDTO;
import project.entity.Order;
import project.mapper.OrderMapper;
import project.model.orderModel.OrderRequest;
import project.model.orderModel.OrderResponse;
import project.repository.OrderHistoryRepository;
import project.repository.OrderRepository;
import project.service.OrderService;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.OrderSpecification.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private Logger logger = LogManager.getLogger("serviceLogger");
    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderHistoryRepository orderHistoryRepository) {
        this.orderRepository = orderRepository;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @Override
    public Page<OrderDTO> getOrders(Pageable pageable) {
        logger.info("getOrders() - Finding orders for page "+ pageable.getPageNumber());
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderDTO> orderDTOS = OrderMapper.ORDER_MAPPER.orderListToOrderDTOList(orders.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOS,pageable,orders.getTotalElements());
        logger.info("getOrders() - Orders were found");
        return orderDTOPage;
    }

    @Override
    public OrderResponse getOrderResponseById(Long id) {
        logger.info("getOrderResponseById() - Finding order for order response by id "+id);
        Order order = orderRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Order was not found by id "+id));
        OrderResponse orderResponse = OrderMapper.ORDER_MAPPER.orderToOrderRequest(order);
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
        List<OrderDTO> orderDTOS = OrderMapper.ORDER_MAPPER.orderListToOrderDTOList(orders.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOS,pageable,orders.getTotalElements());
        logger.info("searchOrders() - Orders were found");
        return orderDTOPage;
    }

    @Override
    public Order gerOrderById(Long id) {
        logger.info("gerOrderById() - Finding order by id "+id);
        Order order = orderRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Order was not found by id "+id));
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
    public void deleteOrderById(Long id, String comment) {
        logger.info("deleteOrderById() - Deleting order by id "+id);
        Order order = orderRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Order was not found by id "+id));
        order.setStatus(OrderStatus.CANCELED);
        String s = "Замовлення скасовано";
        createAndSaveOrderHistory(s,order,comment);
        orderRepository.save(order);
        logger.info("deleteOrderById() - Order was deleted");
    }

    @Override
    public List<Long> getOrdersCountInMonth() {
        logger.info("getOrdersCountInMonth() - Finding orders count in month");
        List<Long> counts = orderRepository.findOrdersCountInMonth();
        logger.info("getOrdersCount() - Orders count was found");
        return counts;
    }

    @Override
    public void updateOrder(OrderRequest orderRequest) {
        logger.info("updateOrder() - Updating order");
        Order orderInDb = orderRepository.findById(orderRequest.getId()).orElseThrow(()-> new EntityNotFoundException("Order was not found by id "+orderRequest.getId()));
        if(!orderInDb.getStatus().equals(orderRequest.getStatus())){
            String s = "Змінено статус "+orderInDb.getStatus().getStatusName()+" на "+orderRequest.getStatus().getStatusName();
            createAndSaveOrderHistory(s,orderInDb);
        }
        orderInDb.setStatus(orderRequest.getStatus());
        orderRepository.save(orderInDb);
        logger.info("updateOrder() - Order was updated");
    }

    @Override
    public void updateOrderWithDelivery(OrderRequest orderRequest, DeliveryDTO deliveryDTO) {
        logger.info("updateOrderWithDelivery() - Updating order with delivery");
        Order orderInDb = orderRepository.findById(orderRequest.getId()).orElseThrow(()-> new EntityNotFoundException("Order was not found by id "+orderRequest.getId()));
        if(!orderInDb.getStatus().equals(orderRequest.getStatus())){
            String s = "Змінено статус "+orderInDb.getStatus().getStatusName()+" на "+orderRequest.getStatus().getStatusName();
            createAndSaveOrderHistory(s,orderInDb);
        }
        orderInDb.setStatus(orderRequest.getStatus());
        if(deliveryDTO != null){
            if(!orderInDb.getDelivery().getName().equals(deliveryDTO.getName())){
                String s = "Змінено імя для доставки з "+orderInDb.getDelivery().getName()+" на "+ deliveryDTO.getName();
                createAndSaveOrderHistory(s,orderInDb);
            }
            orderInDb.getDelivery().setName(deliveryDTO.getName());
            if(!orderInDb.getDelivery().getPhoneNumber().equals(deliveryDTO.getPhoneNumber())){
                String s = "Змінено номер телефону для доставки з "+orderInDb.getDelivery().getPhoneNumber()+" на "+ deliveryDTO.getPhoneNumber();
                createAndSaveOrderHistory(s,orderInDb);
            }
            orderInDb.getDelivery().setPhoneNumber(deliveryDTO.getPhoneNumber());
            if(!orderInDb.getDelivery().getCity().equals(deliveryDTO.getCity())){
                String s = "Змінено місто для доставки з "+orderInDb.getDelivery().getCity()+" на "+ deliveryDTO.getCity();
                createAndSaveOrderHistory(s,orderInDb);
            }
            orderInDb.getDelivery().setCity(deliveryDTO.getCity());
            if(!orderInDb.getDelivery().getBuilding().equals(deliveryDTO.getBuilding())){
                String s = "Змінено будинок для доставки з "+orderInDb.getDelivery().getBuilding()+" на "+ deliveryDTO.getBuilding();
                createAndSaveOrderHistory(s,orderInDb);
            }
            orderInDb.getDelivery().setBuilding(deliveryDTO.getBuilding());
            if(!orderInDb.getDelivery().getStreet().equals(deliveryDTO.getStreet())){
                String s = "Змінено вулицю для доставки з "+orderInDb.getDelivery().getStreet()+" на "+ deliveryDTO.getStreet();
                createAndSaveOrderHistory(s,orderInDb);
            }
            orderInDb.getDelivery().setStreet(deliveryDTO.getStreet());
            if(!orderInDb.getDelivery().getEntrance().equals(deliveryDTO.getEntrance())){
                String s = "Змінено під'їзд для доставки з "+orderInDb.getDelivery().getEntrance()+" на "+ deliveryDTO.getEntrance();
                createAndSaveOrderHistory(s,orderInDb);
            }
            orderInDb.getDelivery().setEntrance(deliveryDTO.getEntrance());
            if(!orderInDb.getDelivery().getApartment().equals(deliveryDTO.getApartment())){
                String s = "Змінено квартиру для доставки з "+orderInDb.getDelivery().getApartment()+" на "+ deliveryDTO.getApartment();
                createAndSaveOrderHistory(s,orderInDb);
            }
            orderInDb.getDelivery().setApartment(deliveryDTO.getApartment());
            if(!orderInDb.getDelivery().getDeliveryDate().equals(deliveryDTO.getDeliveryDate())){
                String s = "Змінено дату для доставки з "+orderInDb.getDelivery().getDeliveryDate()+" на "+ deliveryDTO.getDeliveryDate();
                createAndSaveOrderHistory(s,orderInDb);
            }
            orderInDb.getDelivery().setDeliveryDate(deliveryDTO.getDeliveryDate());
            if(!orderInDb.getDelivery().getDeliveryTime().equals(deliveryDTO.getDeliveryTime())){
                String s = "Змінено час для доставки з "+orderInDb.getDelivery().getDeliveryTime()+" на "+ deliveryDTO.getDeliveryTime();
                createAndSaveOrderHistory(s,orderInDb);
            }
            orderInDb.getDelivery().setDeliveryTime(deliveryDTO.getDeliveryTime());
        }
        orderRepository.save(orderInDb);
        logger.info("updateOrderWithDelivery() - Order with delivery was updated");
    }

    private void createAndSaveOrderHistory(String event, Order order) {
        OrderHistory orderHistory = new OrderHistory(event,LocalDate.now(), LocalTime.now(),order);
        orderHistoryRepository.save(orderHistory);
    }
    private void createAndSaveOrderHistory(String event, Order order,String comment) {
        OrderHistory orderHistory = new OrderHistory(event,LocalDate.now(), LocalTime.now(),order);
        orderHistory.setComment(comment);
        orderHistoryRepository.save(orderHistory);
    }
}
