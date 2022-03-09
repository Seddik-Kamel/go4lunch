package com.example.go4lunch.infrastructure.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.go4lunch.infrastructure.dao.RestaurantDao;
import com.example.go4lunch.infrastructure.entity.RestaurantEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {RestaurantEntity.class}, version = 8, exportSchema = false)

public abstract class RestaurantDatabase extends RoomDatabase {

    public abstract RestaurantDao restaurantDao();

    private static volatile RestaurantDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RestaurantDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RestaurantDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RestaurantDatabase.class, "restaurant_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
