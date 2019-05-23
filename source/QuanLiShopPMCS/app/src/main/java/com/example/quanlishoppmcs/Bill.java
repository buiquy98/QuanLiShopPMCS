package com.example.quanlishoppmcs;

public class Bill {
    private int id;
    private int codePe;
    private String peName;
    private int offPerr;
    private int offPriii;
    private int billPri;
    private String billTime;

    public Bill(){id =0;}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCodePe() {
        return codePe;
    }

    public void setCodePe(int codeP) {
        this.codePe = codeP;
    }

    public String getPeName() {
        return peName;
    }

    public void setPeName(String pName) {
        this.peName = pName;
    }

    public int getoffPerr() {
        return offPerr;
    }

    public void setoffPerr(int offP) {
        this.offPerr = offP;
    }

    public int getoffPriii() { return offPriii;
    }

    public void setoffPriii(int offP) { this.offPriii = offP;
    }

    public int getBillPrir() {
        return billPri;
    }

    public void setBillPri(int billP) {
        this.billPri = billP;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String bt) {
        this.billTime = bt;
    }
}