package com.allrecipes.recipes5.csm.model;

public class offers_model {
    String id;

    public offers_model(String id, String image, String title, String sub, String offer_name, String status) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.sub = sub;
        this.offer_name = offer_name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String image;
    String title;
    String sub;
    String offer_name;
    String status;
}
