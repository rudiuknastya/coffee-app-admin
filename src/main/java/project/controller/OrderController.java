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
import project.entity.OrderItem;
import project.entity.OrderStatus;
import project.model.orderModel.*;
import project.service.OrderItemService;
import project.service.OrderService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private int pageSize = 1;
    private Long orderId;
    @GetMapping("/admin/orders")
    public String orders(Model model){
        model.addAttribute("pageNum", 6);
        model.addAttribute("status", OrderStatus.values());
        return "order/orders";
    }
    @GetMapping("/admin/getOrders")
    public @ResponseBody Page<OrderDTO> getOrders(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderService.getOrders(pageable);
    }
    @GetMapping("/admin/searchOrders")
    public @ResponseBody Page<OrderDTO> searchOrders(@RequestParam("page")int page, @RequestParam(value = "address", required = false)String address, @RequestParam(value = "status", required = false)OrderStatus status, @RequestParam(value = "date", required = false) String stringDate){
        Pageable pageable = PageRequest.of(page, pageSize);
        System.out.println(stringDate);
        LocalDate date = null;
        if(!stringDate.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            date = LocalDate.parse(stringDate, formatter);
        }
        return orderService.searchOrders(pageable, address,status, date);
    }
    @GetMapping("/admin/deleteOrder/{id}")
    public @ResponseBody String deleteOrder(@PathVariable Long id){
        Order order = orderService.gerOrderById(id);
        order.setDeleted(true);
        orderService.saveOrder(order);
        return "success";
    }
    @GetMapping("/admin/orders/edit/getOrderStatuses")
    public @ResponseBody OrderStatusDTO[] getOrderStatuses(){
        OrderStatus[] orderStatuses = OrderStatus.values();
        OrderStatusDTO[] orderStatusDTOS = new OrderStatusDTO[orderStatuses.length];
        for(int i = 0; i < orderStatuses.length; i++){
            OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
            orderStatusDTO.setOrderStatus(orderStatuses[i]);
            orderStatusDTO.setName(orderStatuses[i].getStatusName());
            orderStatusDTOS[i] = orderStatusDTO;
        }
        return orderStatusDTOS;
    }
    @GetMapping("/admin/orders/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model){
        orderId = id;
        System.out.println(orderId);
        model.addAttribute("pageNum", 6);
        return "order/order_page";
    }
    @GetMapping("/admin/orders/edit/getOrder/{id}")
    public @ResponseBody OrderResponse getOrder(@PathVariable Long id){
        return orderService.getOrderResponseById(id);
    }

    @PostMapping("/admin/orders/edit/editOrderDelivery")
    public @ResponseBody List<FieldError> updateOrderDelivery(@ModelAttribute("order") OrderRequest order,@Valid @ModelAttribute("delivery") DeliveryResponse deliveryResponse, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldErrors());
            return bindingResult.getFieldErrors();
        }
        Order orderInDb = orderService.gerOrderById(order.getId());
        orderInDb.setStatus(order.getStatus());
        if(deliveryResponse != null){
            orderInDb.getDelivery().setName(deliveryResponse.getName());
            orderInDb.getDelivery().setPhoneNumber(deliveryResponse.getPhoneNumber());
            orderInDb.getDelivery().setCity(deliveryResponse.getCity());
            orderInDb.getDelivery().setBuilding(deliveryResponse.getBuilding());
            orderInDb.getDelivery().setStreet(deliveryResponse.getStreet());
            orderInDb.getDelivery().setEntrance(deliveryResponse.getEntrance());
            orderInDb.getDelivery().setApartment(deliveryResponse.getApartment());
            orderInDb.getDelivery().setDeliveryDate(deliveryResponse.getDeliveryDate());
            orderInDb.getDelivery().setDeliveryTime(deliveryResponse.getDeliveryTime());
        }
        orderService.saveOrder(orderInDb);
        return null;
    }
    @PostMapping("/admin/orders/edit/editOrder")
    public @ResponseBody String updateOrder(@ModelAttribute("order") OrderRequest order){
        Order orderInDb = orderService.gerOrderById(order.getId());
        orderInDb.setStatus(order.getStatus());
        orderService.saveOrder(orderInDb);
        return "success";
    }
    @GetMapping("/admin/orderItem/edit/{id}")
    public String editOrderItem(@PathVariable Long id, Model model){
        model.addAttribute("pageNum", 6);
        model.addAttribute("orderId", orderId);
        return "order/order_item";
    }

}
