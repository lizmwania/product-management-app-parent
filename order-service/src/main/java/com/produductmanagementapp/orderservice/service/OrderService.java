package com.produductmanagementapp.orderservice.service;

import com.produductmanagementapp.orderservice.dto.InventoryResponse;
import com.produductmanagementapp.orderservice.dto.OrderLineItemsDto;
import com.produductmanagementapp.orderservice.dto.OrderRequest;
import com.produductmanagementapp.orderservice.model.Order;
import com.produductmanagementapp.orderservice.model.OrderLineItems;
import com.produductmanagementapp.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){

        if (orderRequest.getOrderLineItemsList() == null) {
            // Handle the case where orderLineItemsList is null
            // For example, you could throw an exception or log an error
            return;
        }
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        //webclient to call inventory service
        InventoryResponse[] inventoryResponseArray = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
        if(allProductsInStock){
            orderRepository.save(order);
        }else {
            throw new IllegalArgumentException("Product Not Found. Please try again later");
        }

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
