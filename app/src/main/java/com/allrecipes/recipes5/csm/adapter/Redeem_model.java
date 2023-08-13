package com.allrecipes.recipes5.csm.adapter;

public class Redeem_model {
    String image;
    String coins;
    String amount;
    String symbol;
    String input;
    String hint;
    String title;

    public String getAmount_id() {
        return amount_id;
    }

    String amount_id;

    public String getDetails() {
        return details;
    }

    String details;

    public String getType() {
        return type;
    }

    String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public Redeem_model(String image, String coins, String amount, String symbol, String input, String hint, String title,String id,String type, String details,String amount_id) {
        this.image = image;
        this.coins = coins;
        this.amount = amount;
        this.symbol = symbol;
        this.input = input;
        this.hint = hint;
        this.title = title;
        this.id = id;
        this.type = type;
        this.details = details;
        this.amount_id = amount_id;
    }

    public String getImage() {
        return image;
    }

    public String getCoins() {
        return coins;
    }

    public String getAmount() {
        return amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getInput() {
        return input;
    }

    public String getHint() {
        return hint;
    }

    public String getTitle() {
        return title;
    }
}
