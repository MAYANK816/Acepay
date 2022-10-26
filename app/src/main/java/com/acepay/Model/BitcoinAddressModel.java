package com.acepay.Model;

import java.io.Serializable;

/**
 * Created by pro22 on 8/12/17.
 */

public class BitcoinAddressModel implements Serializable {
    String Register_no,bitcoin_address,username,id,profile;

    public String getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegister_no() {
        return Register_no;
    }

    public void setRegister_no(String register_no) {
        Register_no = register_no;
    }

    public String getBitcoin_address() {
        return bitcoin_address;
    }

    public void setBitcoin_address(String bitcoin_address) {
        this.bitcoin_address = bitcoin_address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
