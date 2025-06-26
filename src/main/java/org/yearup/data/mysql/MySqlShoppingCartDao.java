package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.HashMap;
import java.util.Map;

@Component
public class MySqlShoppingCartDao implements ShoppingCartDao
{
    private Map<Integer, ShoppingCart> carts = new HashMap<>();

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        if (!carts.containsKey(userId))
            carts.put(userId, new ShoppingCart());

        return carts.get(userId);
    }

    @Override
    public void addProduct(int userId, int productId)
    {
        ShoppingCart cart = getByUserId(userId);

        if (cart.contains(productId))
        {
            ShoppingCartItem item = cart.get(productId);
            item.setQuantity(item.getQuantity() + 1);
        }
        else
        {
            Product product = new Product(); // You’d actually pull from `ProductDao`
            product.setProductId(productId);
            // You’d fetch more product details from DB here

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(1);

            cart.add(item);
        }
    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity)
    {
        ShoppingCart cart = getByUserId(userId);
        if (cart.contains(productId))
        {
            ShoppingCartItem item = cart.get(productId);
            item.setQuantity(quantity);
        }
    }

    @Override
    public void clearCart(int userId)
    {
        carts.remove(userId);
    }
}
