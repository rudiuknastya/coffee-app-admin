package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.Admin;
import project.model.adminModel.RoleDTO;
import project.model.orderModel.DeliveryDTO;
import project.model.orderModel.OrderDTO;
import project.entity.Order;
import project.model.orderModel.OrderResponse;
import project.model.orderModel.OrderStatusDTO;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper ORDER_MAPPER = Mappers.getMapper(OrderMapper.class);

    List<OrderDTO> orderListToOrderDTOList(List<Order> orders);
    @Mapping(target = "status", expression = "java(order.getStatus().getStatusName())")
    @Mapping(target = "name", expression = "java(order.getUser().getName())")
    @Mapping(target = "locationAddress", expression = "java(order.getLocation().getAddress())")
    OrderDTO orderToOrderDTO(Order order);

    @Mapping(target = "status", expression = "java(createStatusDTO(order))")
    @Mapping(target = "delivery", expression = "java(createDeliveryDTO(order))")
    OrderResponse orderToOrderRequest(Order order);
    default OrderStatusDTO createStatusDTO(Order order) {
        OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
        orderStatusDTO.setOrderStatus(order.getStatus());
        orderStatusDTO.setName(order.getStatus().getStatusName());
        return orderStatusDTO;
    }
    default DeliveryDTO createDeliveryDTO(Order order) {
        if(order.getDelivery() != null){
            return DeliveryMapper.DELIVERY_MAPPER.deliveryToDeliveryDTO(order.getDelivery());
        } else {
            return null;
        }
    }
}
