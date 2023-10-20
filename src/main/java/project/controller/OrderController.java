package project.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project.entity.Order;
import project.entity.OrderStatus;
import project.model.orderModel.OrderAdditive;
import project.model.orderModel.OrderDTO;
import project.model.orderModel.OrderItemDTO;
import project.service.OrderItemService;
import project.service.OrderService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    private int pageSize = 1;
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
    @GetMapping("/admin/orders/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model){
        model.addAttribute("pageNum", 6);
        model.addAttribute("order", orderService.getOrderRequestById(id));
        model.addAttribute("status", OrderStatus.values());
        return "order/order_page";
    }
    @GetMapping("/admin/getOrderItems")
    public @ResponseBody Page<OrderItemDTO> getOrderItems(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderItemService.getOrderItemDTOs( pageable);// 1L
    }
    @GetMapping("/admin/searchOrderItems")
    public @ResponseBody Page<OrderItemDTO> searchOrderItems(@RequestParam("page")int page, @RequestParam(value = "name", required = false)String name){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderItemService.searchOrderItemDTOs(pageable, name);
    }
    @GetMapping("/admin/orderItem/edit/{id}")
    public String editOrderItem(@PathVariable Long id, Model model){
        model.addAttribute("pageNum", 6);
        model.addAttribute("orderItem", orderItemService.getOrderItemRequestById(id));
        return "order/order_item";
    }
    @GetMapping("/admin/getOrderItemAdditives/{orderItemId}")
    public @ResponseBody Page<OrderAdditive> getOrderItemAdditives(@PathVariable Long orderItemId, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderItemService.getOrderAdditives(orderItemId, pageable);
    }
    @GetMapping("/admin/searchOrderItemAdditives/{orderItemId}")
    public @ResponseBody Page<OrderAdditive> searchOrderItemAdditives(@PathVariable Long orderItemId, @RequestParam("page")int page, @RequestParam(value = "name", required = false)String name){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderItemService.searchOrderAdditives(orderItemId, name, pageable);
    }
}
