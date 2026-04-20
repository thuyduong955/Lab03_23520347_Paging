package com.example.android.codelabs.paging.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagingData;
import androidx.paging.PagingDataTransforms;
import com.example.android.codelabs.paging.data.GithubRepository;
import com.example.android.codelabs.paging.model.Repo;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;

public class SearchRepositoriesViewModel extends ViewModel {

    private static final String LAST_QUERY_SCROLLED = "last_query_scrolled";
    private static final String LAST_SEARCH_QUERY = "last_search_query";
    private static final String DEFAULT_QUERY = "Android";

    private final GithubRepository repository;
    private final SavedStateHandle savedStateHandle;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<UiState> _state = new MutableLiveData<>();
    public final LiveData<UiState> state = _state;

    public SearchRepositoriesViewModel(GithubRepository repository, SavedStateHandle savedStateHandle) {
        this.repository = repository;
        this.savedStateHandle = savedStateHandle;

        String initialQuery = savedStateHandle.get(LAST_SEARCH_QUERY);
        if (initialQuery == null) initialQuery = DEFAULT_QUERY;
        
        String lastQueryScrolled = savedStateHandle.get(LAST_QUERY_SCROLLED);
        if (lastQueryScrolled == null) lastQueryScrolled = DEFAULT_QUERY;

        _state.setValue(new UiState(initialQuery, lastQueryScrolled, !initialQuery.equals(lastQueryScrolled)));
    }

    public Flow<PagingData<UiModel>> getPagingDataFlow(String query) {
        return FlowKt.map(repository.getSearchResultStream(query), (pagingData, continuation) -> 
            PagingDataTransforms.map(pagingData, executor, repo -> new UiModel.RepoItem(repo))
        );
    }

    @Override
    protected void onCleared() {
        savedStateHandle.set(LAST_SEARCH_QUERY, state.getValue().getQuery());
        savedStateHandle.set(LAST_QUERY_SCROLLED, state.getValue().getLastQueryScrolled());
        super.onCleared();
    }

    public static class UiState {
        private final String query;
        private final String lastQueryScrolled;
        private final boolean hasNotScrolledForCurrentSearch;

        public UiState(String query, String lastQueryScrolled, boolean hasNotScrolledForCurrentSearch) {
            this.query = query;
            this.lastQueryScrolled = lastQueryScrolled;
            this.hasNotScrolledForCurrentSearch = hasNotScrolledForCurrentSearch;
        }

        public String getQuery() { return query; }
        public String getLastQueryScrolled() { return lastQueryScrolled; }
        public boolean isHasNotScrolledForCurrentSearch() { return hasNotScrolledForCurrentSearch; }
    }

    public abstract static class UiModel {
        public static class RepoItem extends UiModel {
            private final Repo repo;
            public RepoItem(Repo repo) { this.repo = repo; }
            public Repo getRepo() { return repo; }
        }
        public static class SeparatorItem extends UiModel {
            private final String description;
            public SeparatorItem(String description) { this.description = description; }
            public String getDescription() { return description; }
        }
    }
}
