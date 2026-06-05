package com.it.model;

public enum Size {
    S("S", 0),
    M("M", 500),
    L("L", 1000);

    private final String label;
    private final int extraPrice;

    Size(String label, int extraPrice) {
        this.label = label;
        this.extraPrice = extraPrice;
    }

    public String getLabel() {
        return label;
    }

    public int getExtraPrice() {
        return extraPrice;
    }
}
