package com.allrecipes.recipes5.csm.model;

public class r_Model {
    String package_name;
    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public r_Model(String package_name, String p_details, String coins_used, String symbol, String amount, String date, String time, String status, String package_id, String image) {
        this.package_name = package_name;
        this.p_details = p_details;
        this.coins_used = coins_used;
        this.symbol = symbol;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.status = status;
        this.package_id = package_id;
        this.image = image;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getP_details() {
        return p_details;
    }

    public void setP_details(String p_details) {
        this.p_details = p_details;
    }

    public String getCoins_used() {
        return coins_used;
    }

    public void setCoins_used(String coins_used) {
        this.coins_used = coins_used;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    String p_details;
    String coins_used;
    String symbol;
    String amount;
    String date;
    String time;
    String status;
    String package_id;
}
