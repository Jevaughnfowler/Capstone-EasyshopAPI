package org.yearup.data.mysql;

import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MySqlShoppingCartDao implements ShoppingCartDao {

    private final DataSource dataSource;
    private final ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        this.dataSource = dataSource;
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = "SELECT product_id, quantity FROM shopping_cart WHERE user_id = ?";
        Map<Integer, ShoppingCartItem> items = new HashMap<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");

                Product product = productDao.getById(productId);
                if (product == null) {
                    continue; // skip if product not found
                }

                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(quantity);
                item.setDiscountPercent(BigDecimal.ZERO); // assuming no discount for now

                items.put(productId, item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve shopping cart", e);
        }

        ShoppingCart cart = new ShoppingCart();
        cart.setItems(items);
        return cart;
    }

    @Override
    public void addProduct(int userId, int productId) {
        String selectSql = "SELECT quantity FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        String updateSql = "UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, 1)";

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setInt(1, userId);
                selectStmt.setInt(2, productId);

                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Product already in cart for user " + userId + ", updating quantity...");
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, userId);
                        updateStmt.setInt(2, productId);
                        int updatedRows = updateStmt.executeUpdate();
                        System.out.println("Rows updated: " + updatedRows);
                    }
                } else {
                    System.out.println("Product not in cart for user " + userId + ", inserting new...");
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, productId);
                        int insertedRows = insertStmt.executeUpdate();
                        System.out.println("Rows inserted: " + insertedRows);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add product to cart", e);
        }
    }


    @Override
    public void updateQuantity(int userId, int productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, userId);
            stmt.setInt(3, productId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("No cart item found to update");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update cart quantity", e);
        }
    }

    @Override
    public ShoppingCart clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        ShoppingCart shoppingCart = new ShoppingCart();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clear shopping cart", e);
        }
        return shoppingCart;
    }

}
