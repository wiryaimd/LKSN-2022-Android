package com.wiryaimd.pc_bali.model;

public class MenuModel {

    private String id;
    private String nama;
    private int price;
    private byte[] img;

    public MenuModel(String id, String nama, int price, byte[] img) {
        this.id = id;
        this.nama = nama;
        this.price = price;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public int getPrice() {
        return price;
    }

    public byte[] getImg() {
        return img;
    }
}
