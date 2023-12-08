package project.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.entity.Admin;
import project.entity.OrderHistory;
import project.model.orderHistoryModel.OrderHistoryDTO;
import project.model.orderHistoryModel.OrderHistoryResponse;
import project.service.AdminService;
import project.service.OrderHistoryService;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;
    private final AdminService adminService;

    public OrderHistoryController(OrderHistoryService orderHistoryService, AdminService adminService) {
        this.orderHistoryService = orderHistoryService;
        this.adminService = adminService;
    }

    private int pageSize = 5;
    @GetMapping("/orders/orderHistory/{id}")
    public String orderHistories(Model model){
        model.addAttribute("pageNum", 6);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "orderHistory/order_histories";
    }
    @GetMapping("/orders/orderHistory/getOrderHistories/{id}")
    public @ResponseBody Page<OrderHistoryResponse> getOrderHistories(@PathVariable Long id, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderHistoryService.getOrderHistoriesByOrderId(id,pageable);
    }
    @GetMapping("/orders/orderHistory/getOrderHistory/{id}")
    public @ResponseBody OrderHistoryDTO getOrderHistory(@PathVariable Long id){
        return orderHistoryService.getOrderHistoryResponseById(id);
    }
    @PostMapping("/orders/orderHistory/editOrderHistory")
    public @ResponseBody String editOrderHistory(@ModelAttribute("orderHistory") OrderHistoryDTO orderHistoryDTO){
        OrderHistory orderHistory = orderHistoryService.getOrderHistoryById(orderHistoryDTO.getId());
        orderHistory.setComment(orderHistoryDTO.getComment());
        orderHistoryService.saveOrderHistory(orderHistory);
        return "success";
    }
    @GetMapping("/orders/orderHistory/searchOrderHistories")
    public @ResponseBody Page<OrderHistoryResponse> searchOrderHistories(@RequestParam("page")int page, @RequestParam(value = "event", required = false)String event, @RequestParam(value = "date", required = false) LocalDate date, @RequestParam("id") Long id){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderHistoryService.searchOrderHistories(id,event,date, pageable);
    }
}
