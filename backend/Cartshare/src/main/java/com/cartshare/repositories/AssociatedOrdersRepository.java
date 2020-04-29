package com.cartshare.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.cartshare.models.AssociatedOrders;
import com.cartshare.models.Orders;

public interface AssociatedOrdersRepository extends JpaRepository<AssociatedOrders, Long> {
    public List<AssociatedOrders> findByOrder(Orders o);
}