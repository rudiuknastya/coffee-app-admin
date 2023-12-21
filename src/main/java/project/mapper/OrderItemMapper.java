package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    List<OrderItemDTO> orderListToOrderItemDTOList(List<OrderItem> orderItems);
    @Mapping(target = "productName", source = "product.name")
    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);
    OrderItemResponse orderToOrderItemRequest(OrderItem orderItem);
}
