package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.entity.Order;
import project.entity.OrderHistory;
import project.entity.OrderStatus;
import project.model.orderModel.*;
import project.service.OrderHistoryService;
import project.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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
        return orderService.searchOrders(pageable, address,status, date);
    }
    @PostMapping("/deleteOrder/{id}")
    public @ResponseBody ResponseEntity deleteOrder(@PathVariable Long id, @RequestParam("comment")String comment){
        Order order = orderService.gerOrderById(id);
        order.setStatus(OrderStatus.CANCELED);
        String s = "Замовлення скасовано";
        orderHistoryService.createAndSaveOrderHistory(s,order,comment);
        orderService.saveOrder(order);
        return new ResponseEntity(HttpStatus.OK);
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
    public @ResponseBody List<FieldError> updateOrderDelivery(@ModelAttribute("order") OrderRequest order, @Valid @ModelAttribute("delivery") DeliveryDTO deliveryDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return bindingResult.getFieldErrors();
        }
        orderService.updateOrderWithDelivery(order,deliveryDTO);
        return null;
    }
    @PostMapping("/orders/edit/editOrder")
    public @ResponseBody ResponseEntity updateOrder(@ModelAttribute("order") OrderRequest order){
        orderService.updateOrder(order);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/orderItem/edit/{id}")
    public String editOrderItem(@PathVariable Long id, Model model){
        model.addAttribute("pageNum", 6);
        model.addAttribute("orderId", orderId);
        return "order/order_item";
    }

    @GetMapping("/statistic/getOrderArgPriceForMonth")
    public @ResponseBody List<BigDecimal> getOrderArgPriceForMonth(){
        return orderService.getOrderAvrgPricesInMonth();
    }
    @GetMapping("/statistic/getOrdersCount")
    public @ResponseBody Long getOrdersCount(){
        return orderService.getOrdersCount();
    }
    @GetMapping("/statistic/getOrdersCountInMonth")
    public @ResponseBody List<Long> getOrdersCountInMonth(){
        return orderService.getOrdersCountInMonth();
    }
}
