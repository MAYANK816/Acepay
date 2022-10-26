package com.acepay.Model;

import java.io.Serializable;

/**
 * Created by pro22 on 13/1/18.
 */

public class FeesChargeModet implements Serializable{
    String from,to,fees,gst;

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }
}
