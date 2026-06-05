package com.it.model;

public class CartItem {
    private final MenuItem menuItem;
    private int quantity;
    private String options;
    private int unitPrice;

    public CartItem(MenuItem menuItem, int quantity) {
        this(menuItem, quantity, "", menuItem.getPrice());
    }

    public CartItem(MenuItem menuItem, int quantity, String options) {
        this(menuItem, quantity, options, menuItem.getPrice());
    }

    public CartItem(MenuItem menuItem, int quantity, String options, int unitPrice) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.options = options != null ? options : "";
        this.unitPrice = unitPrice;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getTotalPrice() {
        return unitPrice * quantity;
    }
}
