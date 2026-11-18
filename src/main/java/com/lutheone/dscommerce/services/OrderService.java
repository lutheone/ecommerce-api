package com.lutheone.dscommerce.services;

import com.lutheone.dscommerce.dto.OrderDTO;
import com.lutheone.dscommerce.dto.OrderItemDTO;
import com.lutheone.dscommerce.entities.*;
import com.lutheone.dscommerce.repositories.OrderItemRepository;
import com.lutheone.dscommerce.repositories.OrderRepository;
import com.lutheone.dscommerce.repositories.ProductRepository;
import com.lutheone.dscommerce.services.exceptions.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository repository,UserService userService,ProductRepository productRepository,
                        OrderItemRepository orderItemRepository) {
        this.repository = repository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        return repository.findById(id).map(OrderDTO::new).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        User user = userService.authenticated();
        order.setClient(user);

        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }
        repository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(order);
    }

}
