package com.it.service;

import com.it.model.CartItem;
import com.it.model.Order;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private final List<Order> orders = new ArrayList<>();

    public void addOrder(List<CartItem> items, int totalPrice, String paymentMethod) {
        orders.add(new Order(items, totalPrice, paymentMethod));
    }

    public List<Order> getTodayOrders() {
        LocalDate today = LocalDate.now();
        return orders.stream()
                .filter(o -> o.getTimestamp().toLocalDate().isEqual(today))
                .collect(Collectors.toList());
    }

    public List<Order> getAllOrders() {
        return Collections.unmodifiableList(orders);
    }

    public int getTodayTotalSales() {
        return getTodayOrders().stream()
                .mapToInt(Order::getTotalPrice)
                .sum();
    }
}
