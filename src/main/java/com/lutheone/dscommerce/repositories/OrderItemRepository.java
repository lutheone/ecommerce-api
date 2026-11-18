package com.lutheone.dscommerce.repositories;

import com.lutheone.dscommerce.entities.OrderItem;
import com.lutheone.dscommerce.entities.pk.OrderItemPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
