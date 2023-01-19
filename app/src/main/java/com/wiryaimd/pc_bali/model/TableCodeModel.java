package com.wiryaimd.pc_bali.model;

public class TableCodeModel {

    private String id;
    private int number;

    public TableCodeModel(String id, int number) {
        this.id = id;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }
}
