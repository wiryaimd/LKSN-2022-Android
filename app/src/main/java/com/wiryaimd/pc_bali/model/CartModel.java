package com.wiryaimd.pc_bali.model;

public class CartModel {

    private String id;
    private String name;
    private int price;
    private int count;
    private byte[] img;

    public CartModel(String id, String name, int price, int count, byte[] img) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public byte[] getImg() {
        return img;
    }
}
