package org.yearup.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart
{
    private Map<Integer, ShoppingCartItem> items = new HashMap<>();

    public Map<Integer, ShoppingCartItem> getItems()
    {
        return items;
    }

    public void setItems(Map<Integer, ShoppingCartItem> items)
    {
        this.items = items;
    }

    public boolean contains(int productId)
    {
        return items.containsKey(productId);
    }

    public void add(ShoppingCartItem item)
    {
        int productId = item.getProduct().getProductId();

        if (items.containsKey(productId)) {
            // If product already in cart, accumulate quantity
            ShoppingCartItem existingItem = items.get(productId);
            int newQuantity = existingItem.getQuantity() + item.getQuantity();
            existingItem.setQuantity(newQuantity);
        } else {
            items.put(productId, item);
        }
    }

    public ShoppingCartItem get(int productId)
    {
        return items.get(productId);
    }

    public BigDecimal getTotal(double total)
    {
        BigDecimal lineTotal = null;
        return items.values()
                .stream()
                .map(shoppingCartItem -> shoppingCartItem.getLineTotal(lineTotal))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
