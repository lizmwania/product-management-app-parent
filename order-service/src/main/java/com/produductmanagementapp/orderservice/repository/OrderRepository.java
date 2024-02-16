package com.produductmanagementapp.orderservice.repository;

import com.produductmanagementapp.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
