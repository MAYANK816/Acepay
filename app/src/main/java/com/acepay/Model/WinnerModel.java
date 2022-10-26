package com.acepay.Model;

import java.io.Serializable;

public class WinnerModel implements Serializable
{

    String id,name,image,rank,high_Score;

    public String getId()
    {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getHigh_Score() {
        return high_Score;
    }

    public void setHigh_Score(String high_Score) {
        this.high_Score = high_Score;
    }
}
