package com.test;

public class Goods {
    private final long price;
    private int size;

    public Goods(long price, int size) {
        this.price = price;
        this.size = size;
    }

    public long getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
