package com.allrecipes.recipes5.watchandearn.feature;

public class BannerModel {

    String id, image, url;

    public BannerModel(String id, String image, String url) {
        this.id = id;
        this.image = image;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }
}
