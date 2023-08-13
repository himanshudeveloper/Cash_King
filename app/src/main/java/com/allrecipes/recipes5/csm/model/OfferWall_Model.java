package com.allrecipes.recipes5.csm.model;

public class OfferWall_Model {
    String title;
    String disc;
    String image;
    String type;
    public OfferWall_Model(String title, String disc, String image, String type) {
        this.title = title;
        this.disc = disc;
        this.image = image;
        this.type = type;}

    public String getTitle() {
        return title;
    }

    public String getDisc() {
        return disc;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }


}
