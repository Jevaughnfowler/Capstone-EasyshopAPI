package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.models.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrderController
{
    private final OrderDao orderDao;
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;

    @Autowired
    public OrderController(OrderDao orderDao, ShoppingCartDao shoppingCartDao, UserDao userDao)
    {
        this.orderDao = orderDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }

    // POST /orders - place an order from the cart
    @PostMapping
    public Order placeOrder(Principal principal)
    {
        try
        {
            int userId = getUserId(principal);
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);

            if (cart.getItems().isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shopping cart is empty.");

            Order order = new Order();
            order.setUserId(userId);
            order.setOrderDate(LocalDateTime.now());
            order.setTotal(cart.getTotal());

            // Save the order and get generated ID
            Order savedOrder = orderDao.createOrder(order);

            // Save order items
            for (ShoppingCartItem item : cart.getItems().values())
            {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(savedOrder.getOrderId());
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setUnitPrice(item.getProduct().getPrice());

                orderDao.addOrderItem(savedOrder.getOrderId(), orderItem);
            }

            // Clear cart
            shoppingCartDao.clearCart(userId);

            return savedOrder;
        }
        catch (ResponseStatusException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to place order.", e);
        }
    }

    // GET /orders - view user's orders
    @GetMapping
    public List<Order> getOrders(Principal principal)
    {
        try
        {
            int userId = getUserId(principal);
            return orderDao.getOrdersByUserId(userId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve orders.", e);
        }
    }

    private int getUserId(Principal principal)
    {
        String username = principal.getName();
        User user = userDao.getByUserName(username);

        if (user == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found.");

        return user.getId();
    }
}
