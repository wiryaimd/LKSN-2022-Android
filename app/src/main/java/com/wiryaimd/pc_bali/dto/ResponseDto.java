package com.wiryaimd.pc_bali.dto;

public class ResponseDto {

    private String json;
    private byte[] data;
    private int code;

    public ResponseDto(String json, int code) {
        this.json = json;
        this.code = code;
    }

    public ResponseDto(byte[] data, int code) {
        this.data = data;
        this.code = code;
    }

    public String getJson() {
        return json;
    }

    public byte[] getData() {
        return data;
    }

    public int getCode() {
        return code;
    }
}
