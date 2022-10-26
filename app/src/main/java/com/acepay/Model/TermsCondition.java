package com.acepay.Model;

import java.io.Serializable;

/**
 * Created by pro22 on 15/12/17.
 */

public class TermsCondition implements Serializable {

    String term_one,term_two,term_three;

    public String getTerm_one() {
        return term_one;
    }

    public void setTerm_one(String term_one) {
        this.term_one = term_one;
    }

    public String getTerm_two() {
        return term_two;
    }

    public void setTerm_two(String term_two) {
        this.term_two = term_two;
    }

    public String getTerm_three() {
        return term_three;
    }

    public void setTerm_three(String term_three) {
        this.term_three = term_three;
    }
}
