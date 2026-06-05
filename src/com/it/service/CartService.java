package com.it.service;

import com.it.model.CartItem;
import com.it.model.MenuItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartService {
    private final List<CartItem> cartItems = new ArrayList<>();

    public void addItem(MenuItem menuItem) {
        addItem(menuItem, "", menuItem.getPrice());
    }

    public void addItem(MenuItem menuItem, String options) {
        addItem(menuItem, options, menuItem.getPrice());
    }

    public void addItem(MenuItem menuItem, String options, int unitPrice) {
        String opt = options != null ? options : "";
        for (CartItem item : cartItems) {
            if (item.getMenuItem().getName().equals(menuItem.getName())
                    && item.getOptions().equals(opt)
                    && item.getUnitPrice() == unitPrice) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(menuItem, 1, opt, unitPrice));
    }

    public void removeItem(int index) {
        if (index >= 0 && index < cartItems.size()) {
            cartItems.remove(index);
        }
    }

    public void updateQuantity(int index, int quantity) {
        if (index >= 0 && index < cartItems.size() && quantity > 0) {
            cartItems.get(index).setQuantity(quantity);
        }
    }

    public void decreaseQuantity(int index) {
        if (index >= 0 && index < cartItems.size()) {
            CartItem item = cartItems.get(index);
            if (item.getQuantity() <= 1) {
                cartItems.remove(index);
            } else {
                item.setQuantity(item.getQuantity() - 1);
            }
        }
    }

    public void clear() {
        cartItems.clear();
    }

    public int getTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(cartItems);
    }
}
