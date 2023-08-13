package com.allrecipes.recipes5.csm.model;

public class GameModel {
    Integer id;
    String title;
    String image;
    int viewType = 0;

    public GameModel() {
    }

    public GameModel(Integer id, String title, String image, String game_link) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.game_link = game_link;
        this.viewType = 0;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGame_link() {
        return game_link;
    }

    public void setGame_link(String game_link) {
        this.game_link = game_link;
    }

    String game_link;
}
