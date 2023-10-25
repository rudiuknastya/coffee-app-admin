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
import project.entity.OrderItem;
import project.model.orderItemModel.OrderItemResponse;
import project.model.orderModel.DeliveryResponse;
import project.model.orderModel.OrderAdditive;
import project.model.orderItemModel.OrderItemDTO;
import project.service.OrderItemService;

import java.util.List;

@Controller
public class OrderItemController {
    private final OrderItemService orderItemService;
    private int pageSize = 1;
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/admin/getOrderItems/{id}")
    public @ResponseBody Page<OrderItemDTO> getOrderItems(@PathVariable Long id, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderItemService.getOrderItemDTOs(pageable, id);
    }
    @GetMapping("/admin/searchOrderItems/{id}")
    public @ResponseBody Page<OrderItemDTO> searchOrderItems(@PathVariable Long id, @RequestParam("page")int page, @RequestParam(value = "name", required = false)String name){
        Pageable pageable = PageRequest.of(page, pageSize);
        return orderItemService.searchOrderItemDTOs(pageable, id, name);
    }
    @GetMapping("/admin/deleteOrderItem/{id}")
    public @ResponseBody String deleteOrderItem(@PathVariable Long id){
        OrderItem orderItem = orderItemService.getOrderItemById(id);
        orderItem.setDeleted(true);
        orderItemService.saveOrderItem(orderItem);
        return "success";
    }
//    @GetMapping("/admin/orderItem/edit/{id}")
//    public String editOrderItem(@PathVariable Long id, Model model){
//        model.addAttribute("pageNum", 6);
//        return "order/order_item";
//    }
    @GetMapping("/admin/orderItem/edit/getOrderItem/{id}")
    public @ResponseBody OrderItemResponse getOrderItem(@PathVariable Long id){
        return orderItemService.getOrderItemResponseById(id);
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
    @PostMapping("/admin/orderItem/edit/editOrderItem")
    public @ResponseBody List<FieldError> editOrderItem(@Valid @ModelAttribute("orderItem") OrderItemResponse orderItemResponse, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return bindingResult.getFieldErrors();
        }
        OrderItem orderItem = orderItemService.getOrderItemById(orderItemResponse.getId());
        orderItem.setQuantity(orderItemResponse.getQuantity());
        orderItemService.saveOrderItem(orderItem);
        return null;
    }
}
