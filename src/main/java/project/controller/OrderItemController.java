package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.entity.*;
import project.model.additiveModel.AdditiveOrderRequest;
import project.model.additiveModel.AdditiveOrderResponse;
import project.model.additiveModel.AdditiveOrderSelect;
import project.model.orderItemModel.OrderItemResponse;
import project.model.orderModel.OrderAdditive;
import project.model.orderItemModel.OrderItemDTO;
import project.service.AdditiveService;
import project.service.OrderHistoryService;
import project.service.OrderItemService;
import project.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final AdditiveService additiveService;
    private final OrderHistoryService orderHistoryService;
    private int pageSize = 5;

    public OrderItemController(OrderItemService orderItemService, AdditiveService additiveService, OrderHistoryService orderHistoryService) {
        this.orderItemService = orderItemService;
        this.additiveService = additiveService;
        this.orderHistoryService = orderHistoryService;
    }

    @GetMapping("/getOrderItems/{id}")
    public @ResponseBody Page<OrderItemDTO> getOrderItems(@PathVariable Long id, @RequestParam("page")int page,@RequestParam("size")int size){
        Pageable pageable = PageRequest.of(page, size);
        return orderItemService.getOrderItemDTOs(pageable, id);
    }
    @GetMapping("/searchOrderItems/{id}")
    public @ResponseBody Page<OrderItemDTO> searchOrderItems(@PathVariable Long id, @RequestParam("page")int page,@RequestParam("size")int size, @RequestParam(value = "name", required = false)String name){
        Pageable pageable = PageRequest.of(page, size);
        return orderItemService.searchOrderItemDTOs(pageable, id, name);
    }
    @PostMapping("/cancelOrder/{id}")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable Long id, @RequestParam("comment")String comment){
        OrderItem orderItem = orderItemService.getOrderItemById(id);
        String s = "Замовлення скасовано";
        orderHistoryService.createAndSaveOrderHistory(s,orderItem.getOrder(),comment);
        orderItem.setDeleted(true);
        orderItem.getOrder().setStatus(OrderStatus.CANCELED);
        orderItemService.saveOrderItem(orderItem);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/deleteOrderItem/{id}")
    public @ResponseBody ResponseEntity deleteOrderItem(@PathVariable Long id){
        OrderItem orderItem = orderItemService.getOrderItemById(id);
        String s = "Видалено товар "+orderItem.getProduct().getName()+" із замовлення";
        orderHistoryService.createAndSaveOrderHistory(s,orderItem.getOrder());
        orderItem.setDeleted(true);
        orderItemService.saveOrderItem(orderItem);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/checkOrderItem/{id}")
    public @ResponseBody String checkOrderItem(@PathVariable Long id){
        OrderItem orderItem = orderItemService.getOrderItemById(id);
        if(orderItemService.getOrderItemsCount(orderItem.getOrder().getId()).equals(1L)){
            return "wrong";
        }
        return "success";
    }
    @GetMapping("/orderItem/edit/getOrderItem/{id}")
    public @ResponseBody OrderItemResponse getOrderItem(@PathVariable Long id){
        return orderItemService.getOrderItemResponseById(id);
    }
    @GetMapping("/getOrderItemAdditives/{orderItemId}")
    public @ResponseBody Page<OrderAdditive> getOrderItemAdditives(@PathVariable Long orderItemId, @RequestParam("page")int page,@RequestParam("size")int size){
        Pageable pageable = PageRequest.of(page, size);
        return orderItemService.getOrderAdditives(orderItemId, pageable);
    }
    @GetMapping("/searchOrderItemAdditives/{orderItemId}")
    public @ResponseBody Page<OrderAdditive> searchOrderItemAdditives(@PathVariable Long orderItemId, @RequestParam("page")int page,@RequestParam("size")int size, @RequestParam(value = "name", required = false)String name){
        Pageable pageable = PageRequest.of(page, size);
        return orderItemService.searchOrderAdditives(orderItemId, name, pageable);
    }
    @PostMapping("/orderItem/edit/editOrderItem")
    public @ResponseBody ResponseEntity<?> editOrderItem(@Valid @ModelAttribute("orderItem") OrderItemResponse orderItemResponse){
        orderItemService.updateOrderItem(orderItemResponse);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getOrderAdditive/{id}")
    public @ResponseBody AdditiveOrderResponse getOrderAdditive(@PathVariable Long id){
        return additiveService.getAdditiveOrderResponseById(id);
    }

    @GetMapping("/getAdditivesForAdditiveTypeForOrder/{id}")
    public @ResponseBody Page<AdditiveOrderSelect> getAdditivesForAdditiveTypeForOrder(@PathVariable Long id, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1, pageSize);
        return additiveService.getAdditivesForAdditiveTypeForOrder(id, pageable);
    }
    @PostMapping("/orderItem/edit/editOrderItemAdditive")
    public @ResponseBody ResponseEntity editOrderItemAdditive(@ModelAttribute("orderItemAdditive") AdditiveOrderRequest additiveOrderRequest, BindingResult bindingResult, @RequestParam("oldAdditiveId")Long oldAdditiveId){
        orderItemService.updateOrderItemAdditive(additiveOrderRequest,oldAdditiveId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
