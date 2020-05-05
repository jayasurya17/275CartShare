package com.cartshare.Orders.controller;

import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.*;
import com.cartshare.utils.MailController;
import com.cartshare.utils.OrderDetails;

import java.util.*;

import javax.validation.Valid;

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
            } else if (product.isActive() == false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product has been deleted");
            } else if (product.getStore().isActive() == false) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Store has been deleted");
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

            Pool pool = null;
            for (PoolMembers temp: user.getPoolMembers()) {
                if (temp.getStatus().equals("Accepted")) {
                    pool = temp.getPool();
                }
            }
            if (pool == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not part of any pools");
            }
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

    @GetMapping(value = "/pickUp/associatedOrders/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> pickUpAssociatedOrder(@Valid @PathVariable(name = "id") String id) {
        try {
            long l;
            try {
                l = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order ID");
            }
            Orders o = ordersDAO.findOrdersById(l);
            List<Orders> list = ordersDAO.findAssociatedOrders(o);
            if (list == null || list.size() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("There are no associated orders for this order");
            }
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping(value = "/pastOrders/{userId}", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getPastOrdersOfUser(@Valid @PathVariable(name = "userId") String userId) {
        try {
            Long l;
            try {
                l = Long.parseLong(userId);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user id");
            }
            User u = userDAO.findById(l);
            if(u == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user with given ID");
            }
            List<Orders> list = ordersDAO.findOrdersByUser(u);
            if(list == null || list.size() == 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No past orders");
            }
            List<Set<OrderItems>> listOfProductsInOrders = new ArrayList<>();

            for (Orders order : list) {
                listOfProductsInOrders.add(order.getOrderItems());
            }
            return ResponseEntity.status(HttpStatus.OK).body(listOfProductsInOrders);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping(value = "/pickUp/{orderId}", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> pickUpOrder(@Valid @PathVariable(name = "orderId") String orderId) {
        try {
            ordersDAO.updateOrderStatus();
            Long l;
            try {
                l = Long.parseLong(orderId);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order id or user id");
            }
            Orders order = ordersDAO.findOrdersById(l);
            User user = order.getUser();
            if (order == null || user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("order or user with the provided Id doesn't exist");
            }

            if(order.getStatus().compareTo("Confirmed") != 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This order and all it's associated orders have already been picked up");
            }

            order.setStatus("Delivered");

            ordersDAO.saveOrderDetails(order);
            MailController mc = new MailController();
            mc.send(user.getEmail(), "Order #" + order.getId() + " picked up",
                    "Your cartshare order #" + order.getId() + " has been picked up by " + user.getScreenName());
            List<Orders> associated = ordersDAO.findAssociatedOrders(order); // get all associated orders
            // Set<User> hs = new HashSet<User>(); // put users of ass orders in hash set
            // send email to confirm order pick up to all associated poolers
            OrderDetails od = new OrderDetails();
            String message = "";
            if(associated != null){
                for (Orders o : associated) {
                    User u = o.getUser();
                    o.setPickupPooler(user);
                    o.setStatus("PickedUp");
                    ordersDAO.saveOrderDetails(o);
                    Address a = u.getAddress();
                    message += "<h1>User " + u.getScreenName() + "'s order:</h1></br>Address: " + a.getStreet() + ", "
                            + a.getCity() + ", " + a.getState() + ", " + a.getZipcode() + "</br>Order details: </br>"
                            + od.GenerateProductTableWithPrice(o.getOrderItems()) + "</br></br>";
                    // hs.add(o.getUser());
                    mc.send(u.getEmail(), "Order #" + o.getId() + " picked up",
                            "Your cartshare order #" + o.getId() + " has been picked up by " + user.getScreenName());
                }
            }

            // semd email to provide delivery instructions
            mc.sendHTML(user.getEmail(), "Delivery Instructions for the associated orders", message);

            return ResponseEntity.status(HttpStatus.OK).body("Orders picked up");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/deliver/{userId}", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> ordersToDeliver(@Valid @PathVariable(name = "userId") String userId) {
        try{
            long l;
            try{
                l = Long.parseLong(userId);
            } catch(NumberFormatException e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user id");
            }

            User user = userDAO.findById(l);
            if(user == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with ID doesn't exist");
            }

            List<Orders> list = ordersDAO.findOrdersToBeDeliveredByUser(user);
            if(list == null || list.size() == 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No orders to be delivered");
            }
            List<Set<OrderItems>> listOfProductsInOrders = new ArrayList<>();

            for (Orders order : list) {
                listOfProductsInOrders.add(order.getOrderItems());
            }
            return ResponseEntity.status(HttpStatus.OK).body(listOfProductsInOrders);

            // return ResponseEntity.status(HttpStatus.OK).body(list);

        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/deliver/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> deliverOrder(@Valid @PathVariable(name = "id") String id) {

        try {
            Long l;
            try {
                l = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Order ID");
            }
            Orders order = ordersDAO.findOrdersById(l);
            order.setStatus("Delivered");
            ordersDAO.saveOrderDetails(order);
            MailController mc = new MailController();
            mc.send(order.getUser().getEmail(), "Update to your order status",
                    "Your order has been delivered by: " + order.getPickupPooler().getScreenName());
            return ResponseEntity.status(HttpStatus.OK).body("Order status set to delivered");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PutMapping(value = "/notDelivered/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> markAsNotDelivered(@Valid @PathVariable(name = "id") String id) {
        try{
            Long l;
            try {
                l = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Order ID");
            }
            Orders o = ordersDAO.findOrdersById(l);
            if(o == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Order ID");
            }
            o.setStatus("NotDelivered");
            ordersDAO.saveOrderDetails(o);
            User u = o.getUser();
            MailController mc = new MailController();
            mc.send(o.getPickupPooler().getEmail(), "Update about one of the orders you delivered", "User " + u.getScreenName() + " has marked order # " + o.getId() + " as Not Delivered");
            return ResponseEntity.status(HttpStatus.OK).body("Order successfully marked as Not Delivered");
        } catch(Exception e){
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
            } else if (orderItem.getProduct().isActive() == false || orderItem.getProduct().getStore().isActive() == false) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Product or store has been deleted");
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
            } else if (order.getStore().isActive() == false) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not place order. Store has been deleted!");
            }

            for (OrderItems orderItem : order.getOrderItems()) {
                if (orderItem.getProduct().isActive() == false) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not place order. Product " + orderItem.getProduct().getProductName() + " with SKU " + orderItem.getProduct().getSku() + " has been deleted!");
                }
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
            order.setTimestamp(new Date());
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
            
            Pool pool = null;
            for (PoolMembers temp: user.getPoolMembers()) {
                if (temp.getStatus().equals("Accepted")) {
                    pool = temp.getPool();
                }
            }
            if (pool == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not part of any pools");
            }

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

            MailController mc = new MailController();
            OrderDetails od = new OrderDetails();


            Pool pool = null;
            for (PoolMembers temp: user.getPoolMembers()) {
                if (temp.getStatus().equals("Accepted")) {
                    pool = temp.getPool();
                }
            }
            if (pool == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not part of any pools");
            }

            List<Orders> listOfOrders = ordersDAO.findOrdersWithNoPickup(pool, "Ordered", null, store);
            int count = 0;

            String pickupUserMessage = "";

            for (Orders order : listOfOrders) {
                if (order.getUser().getId() != userId) {

                    pickupUserMessage += "<h2>Order " + (count + 1) + " details</h2>";
                    pickupUserMessage += od.GenerateProductTableWithoutPrice(order.getOrderItems());

                    String orderedUserHeading = "A fellow pooler will be picking up your order (# " + order.getId()
                            + ")";
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

            String pickupUserHeading = "You have requested to pick up " + count + " orders";
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
                List<Orders> listOfOrders = ordersDAO.findAllOrdersToBePickedup();
                List<Set<OrderItems>> listOfProductsInOrders = new ArrayList<>();
                if (listOfOrders == null) {
                    return ResponseEntity.status(HttpStatus.OK).body(listOfProductsInOrders);
                }
    
                for (Orders order : listOfOrders) {
                    listOfProductsInOrders.add(order.getOrderItems());
                }
                return ResponseEntity.status(HttpStatus.OK).body(listOfProductsInOrders);
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
            if (listOfOrders == null) {
                return ResponseEntity.status(HttpStatus.OK).body(listOfProductsInOrders);
            }

            for (Orders order : listOfOrders) {
                listOfProductsInOrders.add(order.getOrderItems());
            }
            return ResponseEntity.status(HttpStatus.OK).body(listOfProductsInOrders);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/associatedOrders", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> associatedOrdersDetails(@RequestParam("orderId") String reqOrderId) {
        try {
            Long orderId = null;
            try {
                orderId = Long.parseLong(reqOrderId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order ID");
            }

            Orders order = ordersDAO.findOrdersById(orderId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order ID");
            }

            List<Orders> listOfOrders = ordersDAO.findAssociatedOrders(order);

            List<Set<OrderItems>> listOfProductsInOrders = new ArrayList<>();
            if (listOfOrders == null) {
                return ResponseEntity.status(HttpStatus.OK).body(listOfProductsInOrders);
            }
            for (Orders orderObj : listOfOrders) {
                listOfProductsInOrders.add(orderObj.getOrderItems());
            }
            return ResponseEntity.status(HttpStatus.OK).body(listOfProductsInOrders);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}