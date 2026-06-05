package com.it.service;

import com.it.data.MenuData;
import com.it.model.Category;
import com.it.model.MenuItem;
import java.util.List;

public class AdminService {
    private final MenuData menuData;

    public AdminService(MenuData menuData) {
        this.menuData = menuData;
    }

    public List<MenuItem> getItems() {
        return menuData.getItems();
    }

    public void addItem(String name, int price, Category category) {
        menuData.addItem(name, price, category);
    }

    public void removeItem(int index) {
        menuData.removeItem(index);
    }
}
