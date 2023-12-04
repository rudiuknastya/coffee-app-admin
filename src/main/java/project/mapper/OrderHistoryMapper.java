package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.entity.OrderHistory;
import project.model.orderHistoryModel.OrderHistoryResponse;
@Mapper(componentModel = "spring")
public interface OrderHistoryMapper {
    OrderHistoryMapper ORDER_HISTORY_MAPPER = Mappers.getMapper(OrderHistoryMapper.class);
    OrderHistoryResponse orderHistoryToOrderHistoryResponse(OrderHistory orderHistory);
}
