package com.example.alibaba.Model;

public class Cart {
    public String pid,pname,price,date,discount,time,quantity;
    public Cart() {
    }

    public Cart(String pid, String pname, String price, String date, String discount, String time, String quantity) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.date = date;
        this.discount = discount;
        this.time = time;
        this.quantity = quantity;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
