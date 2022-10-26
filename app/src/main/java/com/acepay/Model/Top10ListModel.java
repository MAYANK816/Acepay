package com.acepay.Model;

import java.io.Serializable;

public class Top10ListModel implements Serializable {
    String id,name,btc_amount,trade,image,date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBtc_amount() {
        return btc_amount;
    }

    public void setBtc_amount(String btc_amount) {
        this.btc_amount = btc_amount;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
