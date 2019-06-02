package com.example.cnpmcs.quanlishoppmcs;


public class Bill {
    private int id;
    private int codeP;
    private String pName;
    private int offPercent;
    private int offPrice;
    private int billPrice;
    private String billTime;

    public Bill(){id =0;}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCodep() {
        return codeP;
    }

    public void setCodep(int codeP) {
        this.codeP = codeP;
    }

    public String getPname() {
        return pName;
    }

    public void setPname(String pName) {
        this.pName = pName;
    }

    public int getOffpe() {
        return offPercent;
    }

    public void setOffpe(int offPercent) {
        this.offPercent = offPercent;
    }

    public int getOffpr() {
        return offPrice;
    }

    public void setOffpr(int offPrice) {
        this.offPrice = offPrice;
    }

    public int getBillpr() {
        return billPrice;
    }

    public void setBillpr(int billPrice) {
        this.billPrice = billPrice;
    }

    public String getBilltime() {
        return billTime;
    }

    public void setBilltime(String billTime) {
        this.billTime = billTime;
    }
}
