package com.pdu.android;

public class Sanpham {
    private int Id;
    private String TenSP;
    private String Mota;
    private byte[] Hinh;

    public Sanpham(int id, String tenSP, String mota, byte[] hinh) {
        Id = id;
        TenSP = tenSP;
        Mota = mota;
        Hinh = hinh;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public String getMota() {
        return Mota;
    }

    public void setMota(String mota) {
        Mota = mota;
    }

    public byte[] getHinh() {
        return Hinh;
    }

    public void setHinh(byte[] hinh) {
        Hinh = hinh;
    }
}