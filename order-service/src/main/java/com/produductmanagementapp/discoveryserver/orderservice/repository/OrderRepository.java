package com.produductmanagementapp.discoveryserver.orderservice.repository;

import com.produductmanagementapp.discoveryserver.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
