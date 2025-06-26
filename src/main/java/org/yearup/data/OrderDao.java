package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.OrderItem;

import java.util.List;

public interface OrderDao
{
    Order createOrder(Order order);
    void addOrderItem(int orderId, OrderItem item);
    List<Order> getOrdersByUserId(int userId);
}
