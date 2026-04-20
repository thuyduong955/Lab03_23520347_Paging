package com.example.android.codelabs.paging.data;

import android.util.Log;
import androidx.annotation.OptIn;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import com.example.android.codelabs.paging.api.GithubService;
import com.example.android.codelabs.paging.db.RepoDatabase;
import com.example.android.codelabs.paging.model.Repo;
import kotlinx.coroutines.flow.Flow;

public class GithubRepository {

    public static final int NETWORK_PAGE_SIZE = 30;

    private final GithubService service;
    private final RepoDatabase database;

    public GithubRepository(GithubService service, RepoDatabase database) {
        this.service = service;
        this.database = database;
    }

    @OptIn(markerClass = ExperimentalPagingApi.class)
    public Flow<PagingData<Repo>> getSearchResultStream(String query) {
        Log.d("GithubRepository", "New query: " + query);

        String dbQuery = "%" + query.replace(' ', '%') + "%";
        
        return new Pager<>(
                new PagingConfig(NETWORK_PAGE_SIZE, NETWORK_PAGE_SIZE, false),
                null,
                new GithubRemoteMediator(query, service, database),
                () -> database.reposDao().reposByName(dbQuery)
        ).getFlow();
    }
}
