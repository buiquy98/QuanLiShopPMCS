package com.example.quanlishoppmcs;

public class Bill {
    private int id;
    private int codep;
    private String pname;
    private int offpe;
    private int offpr;
    private int billpr;
    private String billtime;

    public Bill(){id =0;}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCodep() {
        return codep;
    }

    public void setCodep(int codep) {
        this.codep = codep;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getOffpe() {
        return offpe;
    }

    public void setOffpe(int offpe) {
        this.offpe = offpe;
    }

    public int getOffpr() {
        return offpr;
    }

    public void setOffpr(int offpr) {
        this.offpr = offpr;
    }

    public int getBillpr() {
        return billpr;
    }

    public void setBillpr(int billpr) {
        this.billpr = billpr;
    }

    public String getBilltime() {
        return billtime;
    }

    public void setBilltime(String billtime) {
        this.billtime = billtime;
    }
}