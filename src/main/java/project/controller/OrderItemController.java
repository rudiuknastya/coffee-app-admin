package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private int pageSize = 1;

    public OrderItemController(OrderItemService orderItemService, AdditiveService additiveService, OrderHistoryService orderHistoryService) {
        this.orderItemService = orderItemService;
        this.additiveService = additiveService;
        this.orderHistoryService = orderHistoryService;
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
    @PostMapping("/admin/cancelOrder/{id}")
    public @ResponseBody String cancelOrder(@PathVariable Long id, @RequestParam("comment")String comment){
        OrderItem orderItem = orderItemService.getOrderItemById(id);
        String s = "Замовлення скасовано";
        OrderHistory orderHistory = new OrderHistory(s, LocalDate.now(), LocalTime.now(),orderItem.getOrder());
        orderHistory.setComment(comment);
        orderHistoryService.saveOrderHistory(orderHistory);
        orderItem.setDeleted(true);
        orderItem.getOrder().setStatus(OrderStatus.CANCELED);
        orderItemService.saveOrderItem(orderItem);
        return "success";
    }
    @GetMapping("/admin/deleteOrderItem/{id}")
    public @ResponseBody String deleteOrderItem(@PathVariable Long id){
        OrderItem orderItem = orderItemService.getOrderItemById(id);
        String s = "Видалено товар "+orderItem.getProduct().getName()+" із замовлення";
        OrderHistory orderHistory = new OrderHistory(s, LocalDate.now(), LocalTime.now(),orderItem.getOrder());
        orderHistoryService.saveOrderHistory(orderHistory);
        orderItem.setDeleted(true);
        orderItemService.saveOrderItem(orderItem);
        return "success";
    }

    @GetMapping("/admin/checkOrderItem/{id}")
    public @ResponseBody String checkOrderItem(@PathVariable Long id){
        OrderItem orderItem = orderItemService.getOrderItemById(id);
        if(orderItemService.getOrderItemsCount(orderItem.getOrder().getId()).equals(1L)){
            return "wrong";
        }
        return "success";
    }
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
        if(!orderItem.getQuantity().equals(orderItemResponse.getQuantity())){
            String s = "Змінено кількість товарів у замовленні з "+orderItem.getQuantity()+" на "+orderItemResponse.getQuantity();
            OrderHistory orderHistory = new OrderHistory(s, LocalDate.now(), LocalTime.now(),orderItem.getOrder());
            orderHistoryService.saveOrderHistory(orderHistory);
        }
        BigDecimal p = orderItem.getPrice();
        p = p.divide(new BigDecimal(orderItem.getQuantity()));
        p = p.multiply(new BigDecimal(orderItemResponse.getQuantity()));
        BigDecimal op = orderItem.getOrder().getPrice();
        op = op.subtract(orderItem.getPrice());
        orderItem.setPrice(p);
        orderItem.setQuantity(orderItemResponse.getQuantity());
        op = op.add(orderItem.getPrice());
        orderItem.getOrder().setPrice(op);
        orderItemService.saveOrderItem(orderItem);
        return null;
    }
    @GetMapping("/admin/getOrderAdditive/{id}")
    public @ResponseBody AdditiveOrderResponse getOrderAdditive(@PathVariable Long id){
        return additiveService.getAdditiveOrderResponseById(id);
    }

    @GetMapping("/admin/getAdditivesForAdditiveTypeForOrder/{id}")
    public @ResponseBody Page<AdditiveOrderSelect> getAdditivesForAdditiveTypeForOrder(@PathVariable Long id, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1, pageSize);
        return additiveService.getAdditivesForAdditiveTypeForOrder(id, pageable);
    }
    @PostMapping("/admin/orderItem/edit/editOrderItemAdditive")
    public @ResponseBody List<FieldError> editOrderItemAdditive(@Valid @ModelAttribute("orderItemAdditive") AdditiveOrderRequest additiveOrderRequest, BindingResult bindingResult, @RequestParam("oldAdditiveId")Long oldAdditiveId){
        if(bindingResult.hasErrors()){
            return bindingResult.getFieldErrors();
        }
        Additive newAdditive = additiveService.getAdditiveById(additiveOrderRequest.getAdditiveId());
        OrderItem orderItem = orderItemService.getOrderItemWithAdditivesById(additiveOrderRequest.getOrderItemId());
        int i = 0;
        for(Additive additive: orderItem.getAdditives()){
            if(additive.getId().equals(oldAdditiveId)){
                orderItem.getAdditives().set(i,newAdditive);
                BigDecimal p = orderItem.getPrice();
                BigDecimal op = orderItem.getOrder().getPrice();
                op = op.subtract(orderItem.getPrice());
                p = p.subtract(additive.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
                p = p.add(newAdditive.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
                orderItem.setPrice(p);
                op = op.add(orderItem.getPrice());
                orderItem.getOrder().setPrice(op);
                if(!additive.getName().equals(newAdditive.getName()) ) {
                    String s = "Змінено додаток для товару " + orderItem.getProduct().getName() + " з " + additive.getName() + " на " + newAdditive.getName();
                    OrderHistory orderHistory = new OrderHistory(s, LocalDate.now(), LocalTime.now(), orderItem.getOrder());
                    orderHistoryService.saveOrderHistory(orderHistory);
                }
            }
            i++;
        }
        orderItemService.saveOrderItem(orderItem);
        return null;
    }
}
