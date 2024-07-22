package com.alawnehj.mytvapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.alawnehj.mytvapplication.dao.TvShowDao;
import com.alawnehj.mytvapplication.models.TvShow;

@Database(entities = {TvShow.class},version = 1,exportSchema = false)
public  abstract class TvShowDatabase extends RoomDatabase {
    private static TvShowDatabase tvShowDatabase;

    public static TvShowDatabase getTvshowDatabase(Context context){
        if (tvShowDatabase == null){
            tvShowDatabase = Room.databaseBuilder(context
            ,TvShowDatabase.class
            ,"tv_show_db").build();
        }
        return tvShowDatabase;
    }

    public abstract TvShowDao tvShowDao();


}
