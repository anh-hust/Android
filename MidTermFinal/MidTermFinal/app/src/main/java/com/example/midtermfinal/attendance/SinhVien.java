package com.example.midtermfinal.attendance;

public class SinhVien {
    private String mssv;
    private String ten;

    private int sobuoinghi;

    public SinhVien() {
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }


    public int getSobuoinghi() {
        return sobuoinghi;
    }

    public void setSobuoinghi(int sobuoinghi) {
        this.sobuoinghi = sobuoinghi;
    }
    public void thembuoinghi(){
       sobuoinghi+=1;
    }


    public SinhVien(String mssv, String ten, int sobuoinghi) {
        this.mssv = mssv;
        this.ten = ten;

        this.sobuoinghi = sobuoinghi;

    }




}
