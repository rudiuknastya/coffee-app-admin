package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import project.model.orderModel.DeliveryResponse;
import project.model.orderModel.OrderDTO;
import project.entity.Order;
import project.model.orderModel.OrderResponse;
import project.model.orderModel.OrderStatusDTO;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Named("orderListToOrderDTOList")
    static List<OrderDTO> orderListToOrderDTOList(List<Order> orders){
        if(orders == null){
            return null;
        }
        List<OrderDTO> orderDTOS = new ArrayList<>(orders.size());
        for(Order order: orders){
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setName(order.getUser().getName());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setOrderTime(order.getOrderTime());
            orderDTO.setStatus(order.getStatus().getStatusName());
            orderDTO.setLocationAddress(order.getLocation().getAddress());
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }
    @Named("orderToOrderRequest")
    static OrderResponse orderToOrderRequest(Order order){
        if(order == null){
            return null;
        }
        OrderResponse orderRequest = new OrderResponse();
        orderRequest.setId(order.getId());
        OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
        orderStatusDTO.setOrderStatus(order.getStatus());
        orderStatusDTO.setName(order.getStatus().getStatusName());
        orderRequest.setStatus(orderStatusDTO);
        if(order.getDelivery() != null){
            DeliveryResponse deliveryRequest = new DeliveryResponse();
            deliveryRequest.setName(order.getDelivery().getName());
            deliveryRequest.setPhoneNumber(order.getDelivery().getPhoneNumber());
            deliveryRequest.setCity(order.getDelivery().getCity());
            deliveryRequest.setStreet(order.getDelivery().getStreet());
            deliveryRequest.setBuilding(order.getDelivery().getBuilding());
            deliveryRequest.setEntrance(order.getDelivery().getEntrance());
            deliveryRequest.setApartment(order.getDelivery().getApartment());
            deliveryRequest.setDeliveryDate(order.getDelivery().getDeliveryDate());
            deliveryRequest.setDeliveryTime(order.getDelivery().getDeliveryTime());
            orderRequest.setDelivery(deliveryRequest);
        }
        return orderRequest;
    }
}
