package com.allrecipes.recipes5.csm.model;

public class SliderItems {
    String type; String title;
    String disc;
    String sub_disc;
    String Image;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;


    public SliderItems( String type, String title, String disc, String sub_disc,String url,String Image) {
        this.type = type;
        this.title = title;
        this.disc = disc;
        this.sub_disc = sub_disc;
        this.url = url;
        this.Image = Image;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getSub_disc() {
        return sub_disc;
    }

    public void setSub_disc(String sub_disc) {
        this.sub_disc = sub_disc;
    }
}
