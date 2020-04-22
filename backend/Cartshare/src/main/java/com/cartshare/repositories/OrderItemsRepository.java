package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.cartshare.models.*;

public interface OrderItemsRepository extends JpaRepository<Orders, Long> {

    public OrderItems save(OrderItems orderItems);
    public List<OrderItems> findByOrders(Orders order);
}
