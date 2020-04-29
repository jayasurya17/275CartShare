package com.cartshare.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cartshare.models.AssociatedOrders;

public interface AssociatedOrdersRepository extends JpaRepository<AssociatedOrders, Long> {
    
}