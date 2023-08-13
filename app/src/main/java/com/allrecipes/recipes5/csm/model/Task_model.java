package com.allrecipes.recipes5.csm.model;

public class Task_model {
    String id;
    Integer invites;
    String points;
    String check;
    String title;

    public Integer getRef() {
        return ref;
    }

    public void setRef(Integer ref) {
        this.ref = ref;
    }

    Integer ref;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public Task_model(String id, Integer invites, String points, String check, String title, Integer ref) {
        this.id = id;
        this.invites = invites;
        this.points = points;
        this.check = check;
        this.title = title;
        this.ref = ref;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getInvites() {
        return invites;
    }

    public void setInvites(Integer invites) {
        this.invites = invites;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
