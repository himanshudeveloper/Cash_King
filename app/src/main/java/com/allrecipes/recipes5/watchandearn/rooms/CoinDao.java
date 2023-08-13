package com.allrecipes.recipes5.watchandearn.rooms;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public
interface CoinDao {

    @Query("SELECT * FROM Coins WHERE id IN (:coinId)")
    public Coins getCoins(String coinId);

    @Insert
    public void insert(Coins coins);

    @Query("Update Coins Set coin=:coins WHERE id ='scratch'")
    public void update(String coins);


}
