package com.it.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class Order {
    private final List<CartItem> items;
    private final int totalPrice;
    private final LocalDateTime timestamp;
    private final String paymentMethod;

    public Order(List<CartItem> items, int totalPrice, String paymentMethod) {
        this.items = List.copyOf(items);
        this.totalPrice = totalPrice;
        this.timestamp = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
