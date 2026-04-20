package com.example.android.codelabs.paging.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.android.codelabs.paging.model.Repo;

@Database(
    entities = {Repo.class, RemoteKeys.class},
    version = 1,
    exportSchema = false
)
public abstract class RepoDatabase extends RoomDatabase {

    public abstract RepoDao reposDao();
    public abstract RemoteKeysDao remoteKeysDao();

    private static volatile RepoDatabase INSTANCE;

    public static RepoDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RepoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static RepoDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                RepoDatabase.class, "Github.db"
        ).build();
    }
}
