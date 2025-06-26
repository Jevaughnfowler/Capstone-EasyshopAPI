package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController {

    // a shopping cart requires
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    // each method in this controller requires a Principal object as a parameter
    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // GET method to retrieve the current user's shopping cart
    // https://localhost:8080/cart
    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        try {
            int userId = getUserId(principal);
            return shoppingCartDao.getByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve shopping cart.", e);
        }
    }

    // POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added)
    @PostMapping("/products/{productId}")
    public void addToCart(@PathVariable int productId, Principal principal) {
        try {
            int userId = getUserId(principal);
            shoppingCartDao.addProduct(userId, productId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add product to cart.", e);
        }
    }

    // PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{productId}")
    public void updateQuantity(@PathVariable int productId,
                               @RequestBody ShoppingCartItem item,
                               Principal principal) {
        try {
            if (item.getQuantity() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity cannot be negative.");
            }

            int userId = getUserId(principal);
            shoppingCartDao.updateQuantity(userId, productId, item.getQuantity());
        } catch (ResponseStatusException e) {
            throw e; // allow intentional exceptions through
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update cart item.", e);
        }
    }

    // DELETE method to clear all products from the current user's cart
    // https://localhost:8080/cart
    @DeleteMapping
    public void clearCart(Principal principal) {
        try {
            int userId = getUserId(principal);
            shoppingCartDao.clearCart(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to clear cart.", e);
        }
    }

    // Helper method to extract the userId from Principal
    private int getUserId(Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUserName(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found.");
        }

        return user.getId();
    }
}
