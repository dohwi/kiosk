package com.it.model;

import java.awt.Color;

public enum Category {
    COFFEE("커피", new Color(139, 90, 43)),
    DRINK("음료", new Color(70, 130, 180)),
    DESSERT("디저트", new Color(218, 112, 128)),
    ETC("기타", new Color(142, 142, 142));

    private final String displayName;
    private final Color color;

    Category(String displayName, Color color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
