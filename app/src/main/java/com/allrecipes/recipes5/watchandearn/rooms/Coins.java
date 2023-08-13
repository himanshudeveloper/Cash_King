package com.allrecipes.recipes5.watchandearn.rooms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Coins {
    @PrimaryKey
    @ColumnInfo(name = "coinId")
    public int coinId;
    @ColumnInfo(name = "id", defaultValue = "scratch")
    public String id;
    @ColumnInfo(name = "coin", defaultValue = "100")
    public int coin;

    public Coins(String id, int coin) {
        this.id = id;
        this.coin = coin;
    }

    public String getId() {
        return id;
    }

    public int getCoin() {
        return coin;
    }
}

