package com.example.cnpmcs.quanlishoppmcs;

import java.io.Serializable;


public class Merchadise implements Serializable {
    private int id;
    private String name;
    private int sum;
    private int sell;
    private int buy;
    private int price;
    private String type;
    private String count;
    private String note;
    public int amount;

    public Merchadise(){}

    public Merchadise(int id, String name, int sum, int sell, int buy, int price, String type, String count, String note){
        this.id = id;
        //  this.code = code;
        this.name = name;
        this.sum = sum;
        this.sell = sell;
        this.buy = buy;
        this.price = price;
        this.type = type;
        this.count = count;
        this.note = note;
    }

    public Merchadise(String name, int sum, int sell, int buy, int price, String type, String count, String note){
        this.id = -1;
        //this.code = code;
        this.name = name;
        this.sum = sum;
        this.sell = sell;
        this.buy = buy;
        this.price = price;
        this.type = type;
        this.count = count;
        this.note = note;
    }

    public int getId(){return this.id;}
    public void setId(int id) {this.id=id;}

    //   public String getCode(){return this.code;}
    //   public void setCode(String code) {this.code=code;}

    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    public int getSum(){return this.sum;}
    public void setSum(int sum){this.sum=sum;}

    public int getSell(){return this.sell;}
    public void setSell(int sell){this.sell=sell;}

    public int getBuy(){return this.buy;}
    public void setBuy(int buy){this.buy=buy;}

    public int getPrice(){return this.price;}
    public void setPrice(int price){this.price=price;}

    public String getType(){return this.type;}
    public void setType(String type) {this.type=type;}

    public String getCount(){return this.count;}
    public void setCount(String count) {this.count=count;}

    public String getNote() {return note;}
    public void setNote(String note) {this.note = note;}
}