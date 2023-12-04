package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.OrderItem;
import project.model.orderItemModel.OrderItemDTO;
import project.model.orderItemModel.OrderItemResponse;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemMapper ORDER_ITEM_MAPPER = Mappers.getMapper(OrderItemMapper.class);
    @Named("orderListToOrderItemDTOList")
    static List<OrderItemDTO> orderListToOrderItemDTOList(List<OrderItem> orderItems){
        if(orderItems == null){
            return null;
        }
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>(orderItems.size());
        for(OrderItem orderItem: orderItems){
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setId(orderItem.getId());
            orderItemDTO.setQuantity(orderItem.getQuantity());
            orderItemDTO.setProductName(orderItem.getProduct().getName());
            orderItemDTOS.add(orderItemDTO);
        }
        return orderItemDTOS;
    }
    OrderItemResponse orderToOrderItemRequest(OrderItem orderItem);
}
