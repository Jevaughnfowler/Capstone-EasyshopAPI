package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.OrderItem;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao
{
    public MySqlOrderDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Order createOrder(Order order)
    {
        String sql = "INSERT INTO orders (user_id, order_date, total) VALUES (?, ?, ?)";

        try (Connection conn = getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getUserId());
            ps.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            ps.setBigDecimal(3, order.getTotal());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
            {
                int orderId = rs.getInt(1);
                order.setOrderId(orderId);
            }

            return order;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to create order.", e);
        }
    }

    @Override
    public void addOrderItem(int orderId, OrderItem item)
    {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to add order item.", e);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(int userId)
    {
        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";

        try (Connection conn = getConnection())
        {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotal(rs.getBigDecimal("total"));

                orders.add(order);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to retrieve orders.", e);
        }

        return orders;
    }
}
