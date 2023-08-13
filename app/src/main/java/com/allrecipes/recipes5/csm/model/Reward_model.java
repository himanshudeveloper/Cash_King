package com.allrecipes.recipes5.csm.model;

public class Reward_model {
    String name;
    String image;
    String symbol;
    String hint;
    String input_type;

    public String getArr() {
        return arr;
    }

    public void setArr(String arr) {
        this.arr = arr;
    }

    String arr;

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    Integer minimum;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    String details;
    Integer id;

    public Reward_model(String name, String image, String symbol, String hint, String input_type, Integer id,String details,Integer minimum, String arr) {
        this.name = name;
        this.image = image;
        this.symbol = symbol;
        this.hint = hint;
        this.input_type = input_type;
        this.id = id;
        this.details = details;
        this.minimum = minimum;
        this.arr = arr;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getInput_type() {
        return input_type;
    }

    public void setInput_type(String input_type) {
        this.input_type = input_type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
