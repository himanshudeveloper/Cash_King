package com.allrecipes.recipes5.csm.model;

public class OfferToro_Model {
    String offer_id;
    String offer_name;

    public OfferToro_Model(String offer_id, String offer_name, String offer_desc, String call_to_action, String disclaimer, String offer_url, String offer_url_easy, String payout_type, String amount, String image_url, String image_url_220x124) {
        this.offer_id = offer_id;
        this.offer_name = offer_name;
        this.offer_desc = offer_desc;
        this.call_to_action = call_to_action;
        this.disclaimer = disclaimer;
        this.offer_url = offer_url;
        this.offer_url_easy = offer_url_easy;
        this.payout_type = payout_type;
        this.amount = amount;
        this.image_url = image_url;
        this.image_url_220x124 = image_url_220x124;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getOffer_desc() {
        return offer_desc;
    }

    public void setOffer_desc(String offer_desc) {
        this.offer_desc = offer_desc;
    }

    public String getCall_to_action() {
        return call_to_action;
    }

    public void setCall_to_action(String call_to_action) {
        this.call_to_action = call_to_action;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getOffer_url() {
        return offer_url;
    }

    public void setOffer_url(String offer_url) {
        this.offer_url = offer_url;
    }

    public String getOffer_url_easy() {
        return offer_url_easy;
    }

    public void setOffer_url_easy(String offer_url_easy) {
        this.offer_url_easy = offer_url_easy;
    }

    public String getPayout_type() {
        return payout_type;
    }

    public void setPayout_type(String payout_type) {
        this.payout_type = payout_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url_220x124() {
        return image_url_220x124;
    }

    public void setImage_url_220x124(String image_url_220x124) {
        this.image_url_220x124 = image_url_220x124;
    }

    String offer_desc;
    String call_to_action;
    String disclaimer;
    String offer_url;
    String offer_url_easy;
    String payout_type;
    String amount;
    String image_url;
    String image_url_220x124;
}
