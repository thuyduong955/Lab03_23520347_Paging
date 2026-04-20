package com.example.android.codelabs.paging.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RemoteKeys> remoteKey);

    @Query("SELECT * FROM remote_keys WHERE repoId = :repoId")
    RemoteKeys remoteKeysRepoId(long repoId);

    @Query("DELETE FROM remote_keys")
    void clearRemoteKeys();
}
