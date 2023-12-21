package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.entity.Admin;
import project.entity.Order;
import project.entity.OrderHistory;
import project.entity.OrderStatus;
import project.model.orderModel.*;
import project.service.AdminService;
import project.service.OrderHistoryService;
import project.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final AdminService adminService;

    public OrderController(OrderService orderService, AdminService adminService) {
        this.orderService = orderService;
        this.adminService = adminService;
    }

    private Long orderId;
    @GetMapping("/orders")
    public String orders(Model model){
        model.addAttribute("pageNum", 6);
        model.addAttribute("status", OrderStatus.values());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "order/orders";
    }
    @GetMapping("/getOrders")
    public @ResponseBody Page<OrderDTO> getOrders(@RequestParam("page")int page, @RequestParam("size")int size){
        Pageable pageable = PageRequest.of(page, size);
        return orderService.getOrders(pageable);
    }
    @GetMapping("/searchOrders")
    public @ResponseBody Page<OrderDTO> searchOrders(@RequestParam("page")int page,@RequestParam("size")int size, @RequestParam(value = "address", required = false)String address, @RequestParam(value = "status", required = false)OrderStatus status, @RequestParam(value = "date", required = false) LocalDate date){
        Pageable pageable = PageRequest.of(page, size);
        return orderService.searchOrders(pageable, address,status, date);
    }
    @PostMapping("/deleteOrder/{id}")
    public @ResponseBody ResponseEntity deleteOrder(@PathVariable Long id, @RequestParam("comment")String comment){
        orderService.deleteOrderById(id, comment);
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "order/order_page";
    }
    @GetMapping("/orders/edit/getOrder/{id}")
    public @ResponseBody OrderResponse getOrder(@PathVariable Long id){
        return orderService.getOrderResponseById(id);
    }

    @PostMapping("/orders/edit/editOrderDelivery")
    public @ResponseBody ResponseEntity<?> updateOrderDelivery(@ModelAttribute("order") OrderRequest order, @Valid @ModelAttribute("delivery") DeliveryDTO deliveryDTO){
        orderService.updateOrderWithDelivery(order,deliveryDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/orders/edit/editOrder")
    public @ResponseBody ResponseEntity<?> updateOrder(@ModelAttribute("order") OrderRequest order){
        orderService.updateOrder(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/orderItem/edit/{id}")
    public String editOrderItem(@PathVariable Long id, Model model){
        model.addAttribute("pageNum", 6);
        model.addAttribute("orderId", orderId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
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
