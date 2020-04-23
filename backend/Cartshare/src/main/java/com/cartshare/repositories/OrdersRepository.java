package com.cartshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.cartshare.models.*;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    public List<Orders> findByUser(User user);
}
