package com.lutheone.dscommerce.services;

import com.lutheone.dscommerce.dto.OrderDTO;
import com.lutheone.dscommerce.repositories.OrderRepository;
import com.lutheone.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        return repository.findById(id).map(OrderDTO::new).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

    }


}
