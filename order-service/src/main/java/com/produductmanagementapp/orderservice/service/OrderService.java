package com.produductmanagementapp.orderservice.service;

import com.produductmanagementapp.orderservice.dto.OrderLineItemsDto;
import com.produductmanagementapp.orderservice.dto.OrderRequest;
import com.produductmanagementapp.orderservice.model.Order;
import com.produductmanagementapp.orderservice.model.OrderLineItems;
import com.produductmanagementapp.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        if (orderRequest.getOrderLineItemsList() == null) {
            // Handle the case where orderLineItemsList is null
            // For example, you could throw an exception or log an error
            return;
        }
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = new ArrayList<>();
        for (OrderLineItemsDto dto : orderRequest.getOrderLineItemsList()) {
            OrderLineItems entity = new OrderLineItems();
            entity.setPrice(dto.getPrice());
            entity.setQuantity(dto.getQuantity());
            entity.setSkuCode(dto.getSkuCode());
            // Optionally, you can set other properties here
            orderLineItems.add(entity);
        }

        order.setOrderLineItemsList(orderLineItems);
        orderRepository.save(order);
    }


//    public void placeOrder(OrderRequest orderRequest){
//        Order order = new Order();
//        order.setOrderNumber(UUID.randomUUID().toString());
//        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsList()
//                .stream()
//                .map(this::mapToDto)
//                .toList();
//
//        order.setOrderLineItemsList(orderLineItems);
//        orderRepository.save(order);
//    }

//    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
//        OrderLineItems orderLineItems = new OrderLineItems();
//        orderLineItems.setPrice(orderLineItemsDto.getPrice());
//        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
//        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
//        return  orderLineItems;
//    }

}
