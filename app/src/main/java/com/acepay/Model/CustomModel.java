package com.acepay.Model;

import java.io.Serializable;

/**
 * Created by pro22 on 23/11/17.
 */

public class CustomModel implements Serializable {

    String code,country_name,country_id;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}
