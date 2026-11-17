package com.lutheone.dscommerce.repositories;

import com.lutheone.dscommerce.entities.Order;
import com.lutheone.dscommerce.entities.User;
import com.lutheone.dscommerce.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
