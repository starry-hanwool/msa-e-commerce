package me.hanwool.orderservice.domain.mapper;

import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.orderservice.domain.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

//    @Mapping(target = "orderId", constant = "0L")
    @Mapping(target = "createdDate", ignore = true)
    Orders toOrderEntity(OrderDTO orderDTO);

    OrderDTO ordersToDTO(Orders order);
}
