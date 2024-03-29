package com.produductmanagementapp.discoveryserver.productservice.service;

import com.produductmanagementapp.discoveryserver.productservice.dto.ProductRequest;
import com.produductmanagementapp.discoveryserver.productservice.dto.ProductResponse;
import com.produductmanagementapp.discoveryserver.productservice.model.Product;
import com.produductmanagementapp.discoveryserver.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
//    private  final ProductResponse productResponse;
    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }
    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();

        return products.stream().map(this:: mapToProductResponse).toList();

    }
    private ProductResponse mapToProductResponse (Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
