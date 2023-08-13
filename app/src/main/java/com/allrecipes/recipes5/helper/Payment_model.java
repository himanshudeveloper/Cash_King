package com.allrecipes.recipes5.helper;


public class Payment_model {
    String date,rs,txn;

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public Payment_model() {
    }

    public Payment_model( String date,String rs, String txn ) {

        this.date=date;
        this.rs = rs;
        this.txn = txn;
    }

    public String getDate() {
        return date;
    }

    public String getRs() {
        return rs;
    }


}
