package com.acepay.Model;

import java.io.Serializable;

/**
 * Created by pro22 on 12/12/17.
 */

public class Transaction_model implements Serializable {
    public String getBtc() {
        return btc;
    }

    public void setBtc(String btc) {
        this.btc = btc;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    String id;
    String date;
    String transaction_id;
    String description;
    String amount;
    String btc;
    String rate;
    String status;
    String InrAmount;
    String Receiver_name;
    String Receiver_no;

    public String getTransaction_by() {
        return transaction_by;
    }

    public void setTransaction_by(String transaction_by) {
        this.transaction_by = transaction_by;
    }

    String transaction_by;

    public String getStatus() {
        return status;
    }

    public String getReceiver_name() {
        return Receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        Receiver_name = receiver_name;
    }

    public String getReceiver_no() {
        return Receiver_no;
    }

    public void setReceiver_no(String receiver_no) {
        Receiver_no = receiver_no;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public String getInrAmount() {
        return InrAmount;
    }

    public void setInrAmount(String inrAmount) {
        InrAmount = inrAmount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

}
