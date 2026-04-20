package com.example.android.codelabs.paging.db;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.android.codelabs.paging.model.Repo;
import java.util.List;

@Dao
public interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Repo> repos);

    @Query("SELECT * FROM repos WHERE name LIKE :queryString OR description LIKE :queryString ORDER BY stars DESC, name ASC")
    PagingSource<Integer, Repo> reposByName(String queryString);

    @Query("DELETE FROM repos")
    void clearRepos();
}
