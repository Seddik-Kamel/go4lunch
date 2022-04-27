package com.example.go4lunch.infrastructure.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.go4lunch.infrastructure.dao.LocationDao;
import com.example.go4lunch.infrastructure.dao.RestaurantDao;
import com.example.go4lunch.infrastructure.entity.LocationEntity;
import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.utils.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {RestaurantEntity.class, LocationEntity.class}, version = 14, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class GoLunchDatabase extends RoomDatabase {

    public abstract RestaurantDao restaurantDao();
    public abstract LocationDao locationDao();
    private static volatile GoLunchDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static GoLunchDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GoLunchDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GoLunchDatabase.class, "restaurant_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
