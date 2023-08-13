package com.allrecipes.recipes5.csm.model;

public class Leaderboard_model {
    public Leaderboard_model(String name, String points, String image, String tag) {
        this.name = name;
        this.points = points;
        this.image = image;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public String getPoints() {
        return points;
    }

    public String getImage() {
        return image;
    }

    public String getTag() {
        return tag;
    }

    String name,points,image,tag;
}
