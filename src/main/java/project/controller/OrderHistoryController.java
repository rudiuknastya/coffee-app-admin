package project.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.entity.OrderHistory;
import project.entity.OrderStatus;
import project.model.orderHistoryModel.OrderHistoryResponse;
import project.model.orderModel.OrderDTO;
import project.service.OrderHistoryService;

import java.time.LocalDate;

@Controller
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;

    public OrderHistoryController(OrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }
    private int pageSize = 5;
    @GetMapping("/orders/orderHistory/{id}")
    public String orderHistories(Model model){
        model.addAttribute("pageNum", 6);
        return "orderHistory/order_histories";
    }
    @GetMapping("/orders/orderHistory/getOrderHistories/{id}")
    public @ResponseBody Page<OrderHistory> getOrderHistories(@PathVariable Long id, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderHistoryService.getOrderHistoriesByOrderId(id,pageable);
    }
    @GetMapping("/orders/orderHistory/getOrderHistory/{id}")
    public @ResponseBody OrderHistoryResponse getOrderHistory(@PathVariable Long id){
        return orderHistoryService.getOrderHistoryResponseById(id);
    }
    @PostMapping("/orders/orderHistory/editOrderHistory")
    public @ResponseBody String editOrderHistory(@ModelAttribute("orderHistory") OrderHistoryResponse orderHistoryResponse){
        OrderHistory orderHistory = orderHistoryService.getOrderHistoryById(orderHistoryResponse.getId());
        orderHistory.setComment(orderHistoryResponse.getComment());
        orderHistoryService.saveOrderHistory(orderHistory);
        return "success";
    }
    @GetMapping("/orders/orderHistory/searchOrderHistories")
    public @ResponseBody Page<OrderHistory> searchOrderHistories(@RequestParam("page")int page, @RequestParam(value = "event", required = false)String event, @RequestParam(value = "date", required = false) LocalDate date, @RequestParam("id") Long id){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderHistoryService.searchOrderHistories(id,event,date, pageable);
    }
}
