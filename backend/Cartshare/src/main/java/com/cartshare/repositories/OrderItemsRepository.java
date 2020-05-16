package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.cartshare.models.*;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

    public List<OrderItems> findByOrders(Orders order);
    public OrderItems findOrderItemsByOrdersAndProduct(Orders order, Product product);
    public List<OrderItems> findByProduct(Product product);

}
