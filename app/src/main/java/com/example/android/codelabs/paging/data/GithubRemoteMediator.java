package com.example.android.codelabs.paging.data;

import androidx.annotation.NonNull;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.ListenableFutureRemoteMediator;
import androidx.paging.LoadType;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;
import com.example.android.codelabs.paging.api.GithubService;
import com.example.android.codelabs.paging.api.RepoSearchResponse;
import com.example.android.codelabs.paging.db.RemoteKeys;
import com.example.android.codelabs.paging.db.RepoDatabase;
import com.example.android.codelabs.paging.model.Repo;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

@ExperimentalPagingApi
public class GithubRemoteMediator extends ListenableFutureRemoteMediator<Integer, Repo> {

    private static final int GITHUB_STARTING_PAGE_INDEX = 1;

    private final String query;
    private final GithubService service;
    private final RepoDatabase repoDatabase;

    public GithubRemoteMediator(String query, GithubService service, RepoDatabase repoDatabase) {
        this.query = query;
        this.service = service;
        this.repoDatabase = repoDatabase;
    }

    @NonNull
    @Override
    public ListenableFuture<InitializeAction> initializeFuture() {
        return Futures.immediateFuture(InitializeAction.LAUNCH_INITIAL_REFRESH);
    }

    @NonNull
    @Override
    public ListenableFuture<MediatorResult> loadFuture(@NonNull LoadType loadType, @NonNull PagingState<Integer, Repo> state) {
        SettableFuture<MediatorResult> future = SettableFuture.create();

        int page;
        switch (loadType) {
            case REFRESH:
                RemoteKeys remoteKeys = getRemoteKeyClosestToCurrentPosition(state);
                page = remoteKeys != null && remoteKeys.getNextKey() != null ? remoteKeys.getNextKey() - 1 : GITHUB_STARTING_PAGE_INDEX;
                break;
            case PREPEND:
                RemoteKeys firstRemoteKey = getRemoteKeyForFirstItem(state);
                if (firstRemoteKey == null) {
                    return Futures.immediateFuture(new MediatorResult.Success(false));
                }
                Integer prevKey = firstRemoteKey.getPrevKey();
                if (prevKey == null) {
                    return Futures.immediateFuture(new MediatorResult.Success(true));
                }
                page = prevKey;
                break;
            case APPEND:
                RemoteKeys lastRemoteKey = getRemoteKeyForLastItem(state);
                if (lastRemoteKey == null) {
                    return Futures.immediateFuture(new MediatorResult.Success(false));
                }
                Integer nextKey = lastRemoteKey.getNextKey();
                if (nextKey == null) {
                    return Futures.immediateFuture(new MediatorResult.Success(true));
                }
                page = nextKey;
                break;
            default:
                throw new IllegalArgumentException("Unknown LoadType");
        }

        service.searchRepos(query, page, state.getConfig().pageSize).enqueue(new retrofit2.Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(Call<RepoSearchResponse> call, Response<RepoSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Repo> repos = response.body().getItems();
                    boolean endOfPaginationReached = repos.isEmpty();

                    repoDatabase.runInTransaction(() -> {
                        if (loadType == LoadType.REFRESH) {
                            repoDatabase.remoteKeysDao().clearRemoteKeys();
                            repoDatabase.reposDao().clearRepos();
                        }
                        Integer prevKeyVal = (page == GITHUB_STARTING_PAGE_INDEX) ? null : page - 1;
                        Integer nextKeyVal = endOfPaginationReached ? null : page + 1;
                        List<RemoteKeys> keys = new ArrayList<>();
                        for (Repo repo : repos) {
                            keys.add(new RemoteKeys(repo.getId(), prevKeyVal, nextKeyVal));
                        }
                        repoDatabase.remoteKeysDao().insertAll(keys);
                        repoDatabase.reposDao().insertAll(repos);
                    });
                    future.set(new MediatorResult.Success(endOfPaginationReached));
                } else {
                    future.setException(new IOException("Failed to load data"));
                }
            }

            @Override
            public void onFailure(Call<RepoSearchResponse> call, Throwable t) {
                future.setException(t);
            }
        });

        return future;
    }

    private RemoteKeys getRemoteKeyForLastItem(PagingState<Integer, Repo> state) {
        List<PagingSource.LoadResult.Page<Integer, Repo>> pages = state.getPages();
        for (int i = pages.size() - 1; i >= 0; i--) {
            List<Repo> data = pages.get(i).getData();
            if (!data.isEmpty()) {
                Repo lastRepo = data.get(data.size() - 1);
                return repoDatabase.remoteKeysDao().remoteKeysRepoId(lastRepo.getId());
            }
        }
        return null;
    }

    private RemoteKeys getRemoteKeyForFirstItem(PagingState<Integer, Repo> state) {
        for (PagingSource.LoadResult.Page<Integer, Repo> page : state.getPages()) {
            if (!page.getData().isEmpty()) {
                Repo firstRepo = page.getData().get(0);
                return repoDatabase.remoteKeysDao().remoteKeysRepoId(firstRepo.getId());
            }
        }
        return null;
    }

    private RemoteKeys getRemoteKeyClosestToCurrentPosition(PagingState<Integer, Repo> state) {
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) return null;
        Repo repo = state.closestItemToPosition(anchorPosition);
        return repo == null ? null : repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.getId());
    }
}
