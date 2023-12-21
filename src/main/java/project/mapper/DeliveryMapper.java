package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.entity.Delivery;
import project.model.orderModel.DeliveryDTO;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryMapper DELIVERY_MAPPER = Mappers.getMapper(DeliveryMapper.class);
    DeliveryDTO deliveryToDeliveryDTO(Delivery delivery);
}
