package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.entity.Order;
import project.entity.OrderHistory;
import project.entity.OrderItem;
import project.entity.OrderStatus;
import project.model.orderModel.*;
import project.service.OrderHistoryService;
import project.service.OrderItemService;
import project.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final OrderHistoryService orderHistoryService;

    public OrderController(OrderService orderService, OrderHistoryService orderHistoryService) {
        this.orderService = orderService;
        this.orderHistoryService = orderHistoryService;
    }

    private int pageSize = 1;
    private Long orderId;
    @GetMapping("/orders")
    public String orders(Model model){
        model.addAttribute("pageNum", 6);
        model.addAttribute("status", OrderStatus.values());
        return "order/orders";
    }
    @GetMapping("/getOrders")
    public @ResponseBody Page<OrderDTO> getOrders(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderService.getOrders(pageable);
    }
    @GetMapping("/searchOrders")
    public @ResponseBody Page<OrderDTO> searchOrders(@RequestParam("page")int page, @RequestParam(value = "address", required = false)String address, @RequestParam(value = "status", required = false)OrderStatus status, @RequestParam(value = "date", required = false) LocalDate date){
        Pageable pageable = PageRequest.of(page, pageSize);
        System.out.println(date);
        return orderService.searchOrders(pageable, address,status, date);
    }
    @PostMapping("/deleteOrder/{id}")
    public @ResponseBody String deleteOrder(@PathVariable Long id, @RequestParam("comment")String comment){
        Order order = orderService.gerOrderById(id);
        order.setStatus(OrderStatus.CANCELED);
        String s = "Замовлення скасовано";
        OrderHistory orderHistory = new OrderHistory(s, LocalDate.now(), LocalTime.now(),order);
        orderHistory.setComment(comment);
        orderHistoryService.saveOrderHistory(orderHistory);
        orderService.saveOrder(order);
        return "success";
    }
    @GetMapping("/orders/edit/getOrderStatuses")
    public @ResponseBody OrderStatusDTO[] getOrderStatuses(){
        OrderStatus[] orderStatuses = OrderStatus.values();
        OrderStatusDTO[] orderStatusDTOS = new OrderStatusDTO[orderStatuses.length-1];
        for(int i = 0; i < orderStatuses.length-1; i++){
            OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
            orderStatusDTO.setOrderStatus(orderStatuses[i]);
            orderStatusDTO.setName(orderStatuses[i].getStatusName());
            orderStatusDTOS[i] = orderStatusDTO;
        }
        return orderStatusDTOS;
    }
    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model){
        orderId = id;
        model.addAttribute("pageNum", 6);
        return "order/order_page";
    }
    @GetMapping("/orders/edit/getOrder/{id}")
    public @ResponseBody OrderResponse getOrder(@PathVariable Long id){
        return orderService.getOrderResponseById(id);
    }

    @PostMapping("/orders/edit/editOrderDelivery")
    public @ResponseBody List<FieldError> updateOrderDelivery(@ModelAttribute("order") OrderRequest order,@Valid @ModelAttribute("delivery") DeliveryResponse deliveryResponse, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldErrors());
            return bindingResult.getFieldErrors();
        }
        Order orderInDb = orderService.gerOrderById(order.getId());
        orderInDb.setStatus(order.getStatus());
        if(!orderInDb.getStatus().equals(order.getStatus())){
            String s = "Змінено статус "+orderInDb.getStatus().getStatusName()+" на "+order.getStatus().getStatusName();
            OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
            orderHistoryService.saveOrderHistory(orderHistory);
        }
        if(deliveryResponse != null){
            if(!orderInDb.getDelivery().getName().equals(deliveryResponse.getName())){
                String s = "Змінено імя для доставки з "+orderInDb.getDelivery().getName()+" на "+deliveryResponse.getName();
                OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
                orderHistoryService.saveOrderHistory(orderHistory);
            }
            orderInDb.getDelivery().setName(deliveryResponse.getName());
            if(!orderInDb.getDelivery().getPhoneNumber().equals(deliveryResponse.getPhoneNumber())){
                String s = "Змінено номер телефону для доставки з "+orderInDb.getDelivery().getPhoneNumber()+" на "+deliveryResponse.getPhoneNumber();
                OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
                orderHistoryService.saveOrderHistory(orderHistory);
            }
            orderInDb.getDelivery().setPhoneNumber(deliveryResponse.getPhoneNumber());
            if(!orderInDb.getDelivery().getCity().equals(deliveryResponse.getCity())){
                String s = "Змінено місто для доставки з "+orderInDb.getDelivery().getCity()+" на "+deliveryResponse.getCity();
                OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
                orderHistoryService.saveOrderHistory(orderHistory);
            }
            orderInDb.getDelivery().setCity(deliveryResponse.getCity());
            if(!orderInDb.getDelivery().getBuilding().equals(deliveryResponse.getBuilding())){
                String s = "Змінено будинок для доставки з "+orderInDb.getDelivery().getBuilding()+" на "+deliveryResponse.getBuilding();
                OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
                orderHistoryService.saveOrderHistory(orderHistory);
            }
            orderInDb.getDelivery().setBuilding(deliveryResponse.getBuilding());
            if(!orderInDb.getDelivery().getStreet().equals(deliveryResponse.getStreet())){
                String s = "Змінено вулицю для доставки з "+orderInDb.getDelivery().getStreet()+" на "+deliveryResponse.getStreet();
                OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
                orderHistoryService.saveOrderHistory(orderHistory);
            }
            orderInDb.getDelivery().setStreet(deliveryResponse.getStreet());
            if(!orderInDb.getDelivery().getEntrance().equals(deliveryResponse.getEntrance())){
                String s = "Змінено під'їзд для доставки з "+orderInDb.getDelivery().getEntrance()+" на "+deliveryResponse.getEntrance();
                OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
                orderHistoryService.saveOrderHistory(orderHistory);
            }
            orderInDb.getDelivery().setEntrance(deliveryResponse.getEntrance());
            if(!orderInDb.getDelivery().getApartment().equals(deliveryResponse.getApartment())){
                String s = "Змінено квартиру для доставки з "+orderInDb.getDelivery().getApartment()+" на "+deliveryResponse.getApartment();
                OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
                orderHistoryService.saveOrderHistory(orderHistory);
            }
            orderInDb.getDelivery().setApartment(deliveryResponse.getApartment());
            if(!orderInDb.getDelivery().getDeliveryDate().equals(deliveryResponse.getDeliveryDate())){
                String s = "Змінено дату для доставки з "+orderInDb.getDelivery().getDeliveryDate()+" на "+deliveryResponse.getDeliveryDate();
                OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
                orderHistoryService.saveOrderHistory(orderHistory);
            }
            orderInDb.getDelivery().setDeliveryDate(deliveryResponse.getDeliveryDate());
            if(!orderInDb.getDelivery().getDeliveryTime().equals(deliveryResponse.getDeliveryTime())){
                String s = "Змінено час для доставки з "+orderInDb.getDelivery().getDeliveryTime()+" на "+deliveryResponse.getDeliveryTime();
                OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
                orderHistoryService.saveOrderHistory(orderHistory);
            }
            orderInDb.getDelivery().setDeliveryTime(deliveryResponse.getDeliveryTime());
        }
        orderService.saveOrder(orderInDb);
        return null;
    }
    @PostMapping("/orders/edit/editOrder")
    public @ResponseBody String updateOrder(@ModelAttribute("order") OrderRequest order){
        Order orderInDb = orderService.gerOrderById(order.getId());
        if(!orderInDb.getStatus().equals(order.getStatus())){
            String s = "Змінено статус "+orderInDb.getStatus().getStatusName()+" на "+order.getStatus().getStatusName();
            OrderHistory orderHistory = new OrderHistory(s,LocalDate.now(), LocalTime.now(),orderInDb);
            orderHistoryService.saveOrderHistory(orderHistory);
        }
        orderInDb.setStatus(order.getStatus());
        orderService.saveOrder(orderInDb);
        return "success";
    }
    @GetMapping("/orderItem/edit/{id}")
    public String editOrderItem(@PathVariable Long id, Model model){
        model.addAttribute("pageNum", 6);
        model.addAttribute("orderId", orderId);
        return "order/order_item";
    }

    @GetMapping("/getOrderArgPriceForMonth")
    public @ResponseBody List<BigDecimal> getOrderArgPriceForMonth(){
        return orderService.getOrderAvrgPricesInMonth();
    }
    @GetMapping("/getOrdersCount")
    public @ResponseBody Long getOrdersCount(){
        return orderService.getOrdersCount();
    }
    @GetMapping("/getOrdersCountInMonth")
    public @ResponseBody List<Long> getOrdersCountInMonth(){
        return orderService.getOrdersCountInMonth();
    }
}
