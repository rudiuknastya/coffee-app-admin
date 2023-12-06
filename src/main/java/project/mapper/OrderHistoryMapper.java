package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.entity.OrderHistory;
import project.model.orderHistoryModel.OrderHistoryDTO;
import project.model.orderHistoryModel.OrderHistoryResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderHistoryMapper {
    OrderHistoryMapper ORDER_HISTORY_MAPPER = Mappers.getMapper(OrderHistoryMapper.class);
    OrderHistoryDTO orderHistoryToOrderHistoryDTO(OrderHistory orderHistory);
    List<OrderHistoryResponse> orderHistoryToOrderHistoryResponse(List<OrderHistory> orderHistories);
}
