package com.acepay.Model;

import java.io.Serializable;

/**
 * Created by pro22 on 16/1/18.
 */

public class AdvertismentImages implements Serializable{
    String title,id,description,image,advertisement_url;

    public String getAdvertisement_url() {
        return advertisement_url;
    }

    public void setAdvertisement_url(String advertisement_url) {
        this.advertisement_url = advertisement_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
