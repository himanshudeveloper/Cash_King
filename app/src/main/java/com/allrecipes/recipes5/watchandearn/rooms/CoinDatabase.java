package com.allrecipes.recipes5.watchandearn.rooms;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Coins.class}, version = 1)
public abstract class CoinDatabase extends RoomDatabase {

    public abstract CoinDao getDao();


}
