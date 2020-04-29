package com.cartshare.Orders.controller;

import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.*;
import com.cartshare.utils.MailController;
import com.cartshare.utils.OrderDetails;

import java.util.*;
import com.cartshare.RequestBody.*;
import com.cartshare.Store.dao.StoreDAO;
import com.cartshare.Orders.dao.OrdersDAO;
import com.cartshare.Pool.dao.PoolDAO;
import com.cartshare.Product.dao.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    OrdersDAO ordersDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ProductDAO productDAO;

    @Autowired
    StoreDAO storeDAO;

    @Autowired
    PoolDAO poolDAO;

    @PostMapping(value = "/add/cart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> addProductToCart(@RequestBody OrderRequest orderRequest) {

        try {
            Long userId = null;
            try {
                userId = Long.parseLong(orderRequest.getUserId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Long storeId = null;
            try {
                storeId = Long.parseLong(orderRequest.getStoreId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }
            Long productId = null;
            try {
                productId = Long.parseLong(orderRequest.getProductId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID");
            }
            Product product = productDAO.findById(productId);
            if (product == null || product.getStore().getId() != storeId) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not belong to the store");
            }
            Long quantity = null;
            try {
                quantity = Long.parseLong(orderRequest.getQuantity());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quantity");
            }
            if (quantity < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quantity");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            if (user.getPoolMembers().size() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not part of any pools");
            }

            Pool pool = user.getPoolMembers().iterator().next().getPool();
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                order = new Orders();
                order.setUser(user);
                order.setPool(pool);
                order.setStore(storeDAO.findById(storeId));
                order.setStatus("Cart");
                order = ordersDAO.saveOrderDetails(order);
            } else if (order.getStore().getId() != storeId) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Cart cannot have products from multiple stores");
            }

            OrderItems orderItem = ordersDAO.findOrderItemsByOrdersAndProduct(order, product);
            if (orderItem == null) {
                orderItem = new OrderItems(order, product, quantity);
            } else {
                orderItem.setQuantity(orderItem.getQuantity() + quantity);
            }
            return ResponseEntity.status(HttpStatus.OK).body(ordersDAO.saveOrderItem(orderItem));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/activeStoreInCart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getactiveStoreInCart(@RequestParam(value = "userId") String reqUserId) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(reqUserId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order in cart");
            }

            return ResponseEntity.status(HttpStatus.OK).body(order.getStore());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/productsInCart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getProductsInCart(@RequestParam(value = "userId") String reqUserId) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(reqUserId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order in cart");
            }

            return ResponseEntity.status(HttpStatus.OK).body(order.getOrderItems());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(value = "/update/cart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> updateQuantityOfProductInCart(@RequestBody OrderRequest orderRequest) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(orderRequest.getUserId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Long quantity = null;
            try {
                quantity = Long.parseLong(orderRequest.getQuantity());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quantity");
            }
            if (quantity < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quantity");
            }
            Long orderItemId = null;
            try {
                orderItemId = Long.parseLong(orderRequest.getOrderItemId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order item ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order in cart");
            }

            OrderItems orderItem = ordersDAO.findOrderItemsById(orderItemId);
            if (orderItem == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not exist in order");
            }
            orderItem.setQuantity(quantity);
            return ResponseEntity.status(HttpStatus.OK).body(ordersDAO.saveOrderItem(orderItem));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/removeProductFromCart", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> removeProductFromCart(@RequestParam(value = "userId") String reqUserId,
            @RequestParam(value = "orderItemId") String reqOrderItemId) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(reqUserId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            Long orderItemId = null;
            try {
                orderItemId = Long.parseLong(reqOrderItemId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order item ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order in cart");
            }

            OrderItems orderItem = ordersDAO.findOrderItemsById(orderItemId);
            if (orderItem == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not exist in order");
            }
            ordersDAO.removeProductFromCart(orderItemId);
            List<OrderItems> itemsPresentInOrder = ordersDAO.findProductsInAOrder(order);
            if (itemsPresentInOrder.size() == 0) {
                ordersDAO.removeOrderFromCart(order.getId());
            }

            return ResponseEntity.status(HttpStatus.OK).body("Product removed from cart");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(value = "/confirmOrder", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> confirmOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(orderRequest.getUserId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
            Orders order = ordersDAO.findOrdersByUserAndStatus(user, "Cart");
            if (order == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not have an active order in cart");
            }

            for (OrderItems orderItem : order.getOrderItems()) {
                orderItem.setProductName(orderItem.getProduct().getProductName());
                orderItem.setProductPrice(orderItem.getProduct().getPrice());
                orderItem.setProductImage(orderItem.getProduct().getImageURL());
                orderItem.setProductBrand(orderItem.getProduct().getBrand());
                orderItem.setProductUnit(orderItem.getProduct().getUnit());
                ordersDAO.saveOrderItem(orderItem);
            }

            if (orderRequest.getSelfPickup() == true) {
                order.setPickupPooler(user);
                order.setStatus("Confirmed");
            } else {
                order.setStatus("Ordered");
            }
            order = ordersDAO.saveOrderDetails(order);

            MailController mc = new MailController();
            OrderDetails od = new OrderDetails();
            String heading = "Your order has been placed (#" + order.getId() + ")";
            String message = od.GenerateProductTableWithPrice(order.getOrderItems());
            System.out.println(message);
            mc.sendHTML(user.getEmail(), heading, message);

            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/pendingInPool", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getPendingOrdersInPool(@RequestParam(value = "userId") String reqUserId,
            @RequestParam(value = "storeId") String reqStoreId) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(reqUserId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            Long storeId = null;
            try {
                storeId = Long.parseLong(reqStoreId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }
            Store store = storeDAO.findById(storeId);
            if (store == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }

            if (user.getPoolMembers().size() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not part of any pools");
            }
            Pool pool = user.getPoolMembers().iterator().next().getPool();

            List<Orders> listOfOrders = ordersDAO.findOrdersWithNoPickup(pool, "Ordered", null, store);
            Integer count = 0;
            List<Set<OrderItems>> listOfProductsInOrders = new ArrayList<>();
            for (Orders order : listOfOrders) {
                if (order.getUser().getId() != userId) {
                    listOfProductsInOrders.add(order.getOrderItems());
                    count++;
                    if (count == 10) {
                        break;
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(listOfProductsInOrders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(value = "/pickupOtherOrders", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> pickupOtherOrders(@RequestBody OrderRequest orderRequest) {
        try {
            Long userId = null;
            try {
                userId = Long.parseLong(orderRequest.getUserId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            Long storeId = null;
            try {
                storeId = Long.parseLong(orderRequest.getStoreId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }
            Store store = storeDAO.findById(storeId);
            if (store == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid store ID");
            }

            Long orderId = null;
            try {
                orderId = Long.parseLong(orderRequest.getOrderId());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order ID");
            }
            Orders originalOrder = ordersDAO.findOrdersById(orderId);
            Integer numberOfOrders;
            try {
                numberOfOrders = Integer.parseInt(orderRequest.getNumberOfOrders());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid number of orders");
            }
            if (numberOfOrders < 1 || numberOfOrders > 10) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid number of orders");
            }

            if (user.getPoolMembers().size() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not part of any pools");
            }
            MailController mc = new MailController();
            OrderDetails od = new OrderDetails();

            Pool pool = user.getPoolMembers().iterator().next().getPool();
            List<Orders> listOfOrders = ordersDAO.findOrdersWithNoPickup(pool, "Ordered", null, store);
            int count = 0;

            String pickupUserMessage = "";

            for (Orders order : listOfOrders) {
                if (order.getUser().getId() != userId) {

                    pickupUserMessage += "<h2>Order " + (count + 1) + " details</h2>";
                    pickupUserMessage += od.GenerateProductTableWithoutPrice(order.getOrderItems());

                    String orderedUserHeading = "A fellow pooler will be picking up your order (# " + order.getId() + ")";
                    String orderedUserMessage = od.GenerateProductTableWithPrice(order.getOrderItems());
                    mc.sendHTML(order.getUser().getEmail(), orderedUserHeading, orderedUserMessage);

                    order.setStatus("Confirmed");
                    order.setPickupPooler(user);
                    ordersDAO.saveOrderDetails(order);

                    AssociatedOrders associatedOrders = new AssociatedOrders(originalOrder, order);
                    ordersDAO.saveAssociatedOrders(associatedOrders);

                    count++;
                    if (count == numberOfOrders) {
                        break;
                    }
                }
            }

            String pickupUserHeading = "You have picked up " + count + " orders";
            mc.sendHTML(user.getEmail(), pickupUserHeading, pickupUserMessage);

            return ResponseEntity.status(HttpStatus.OK).body(pickupUserHeading);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/ordersToPickup", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getOrdersToPickup(@RequestParam(value = "userId", required = false) String reqUserId) {
        try {
            if (reqUserId == null) {
                return ResponseEntity.status(HttpStatus.OK).body(ordersDAO.findAllOrdersToBePickedup());
            }
            Long userId = null;
            try {
                userId = Long.parseLong(reqUserId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }

            List<Orders> listOfOrders = ordersDAO.findOrdersToBePickedUpByUser(user);

            List<Set<OrderItems>> listOfProductsInOrders = new ArrayList<>();
            for (Orders order : listOfOrders) {
                listOfProductsInOrders.add(order.getOrderItems());
            }
            return ResponseEntity.status(HttpStatus.OK).body(listOfProductsInOrders);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}