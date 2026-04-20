package com.example.android.codelabs.paging.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "remote_keys")
public class RemoteKeys {
    @PrimaryKey
    private final long repoId;
    private final Integer prevKey;
    private final Integer nextKey;

    public RemoteKeys(long repoId, Integer prevKey, Integer nextKey) {
        this.repoId = repoId;
        this.prevKey = prevKey;
        this.nextKey = nextKey;
    }

    public long getRepoId() {
        return repoId;
    }

    public Integer getPrevKey() {
        return prevKey;
    }

    public Integer getNextKey() {
        return nextKey;
    }
}
