package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.cartshare.models.*;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    public List<Orders> findByUser(User user);
    public Orders findOrdersByUserAndStatus(User user, String status);
    public List<Orders> findAllOrdersByPoolAndStatusAndPickupPoolerAndStore(Pool pool, String status, User pickupUser, Store store);
    public List<Orders> findAllOrdersByPoolAndStoreAndStatus(Pool pool, Store store, String status);
    public List<Orders> findAllOrdersByPoolAndStoreAndPickupPooler(Pool pool, Store store, User pickupUser);
    public List<Orders> findAllOrdersByPickupPoolerAndStatus(User user, String status);
}
