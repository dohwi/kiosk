package com.it.data;

import com.it.model.Category;
import com.it.model.MenuItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MenuData {
    private final List<MenuItem> items = new ArrayList<>();

    public MenuData() {
        items.add(new MenuItem("아메리카노", 4500, Category.COFFEE));
        items.add(new MenuItem("카페라떼", 5000, Category.COFFEE));
        items.add(new MenuItem("바닐라라떼", 5500, Category.COFFEE));
        items.add(new MenuItem("레몬에이드", 4500, Category.DRINK));
        items.add(new MenuItem("자몽에이드", 5000, Category.DRINK));
        items.add(new MenuItem("초콜릿 케이크", 6000, Category.DESSERT));
        items.add(new MenuItem("마카롱", 3000, Category.DESSERT));
        items.add(new MenuItem("크로플", 4500, Category.DESSERT));
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<MenuItem> getItemsByCategory(Category category) {
        return items.stream()
                .filter(item -> item.getCategory() == category)
                .collect(Collectors.toList());
    }

    public void addItem(String name, int price, Category category) {
        items.add(new MenuItem(name, price, category));
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }
}
