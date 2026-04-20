package com.example.android.codelabs.paging.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.AbstractSavedStateViewModelFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.savedstate.SavedStateRegistryOwner;
import com.example.android.codelabs.paging.data.GithubRepository;

public class ViewModelFactory extends AbstractSavedStateViewModelFactory {

    private final GithubRepository repository;

    public ViewModelFactory(SavedStateRegistryOwner owner, GithubRepository repository) {
        super(owner, null);
        this.repository = repository;
    }

    @NonNull
    @Override
    protected <T extends ViewModel> T create(@NonNull String key, @NonNull Class<T> modelClass, @NonNull SavedStateHandle handle) {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel.class)) {
            return (T) new SearchRepositoriesViewModel(repository, handle);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
