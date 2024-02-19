package com.produductmanagementapp.discoveryserver.productservice.repository;

import com.produductmanagementapp.discoveryserver.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
