package com.example.android.codelabs.paging;

import android.content.Context;
import androidx.lifecycle.ViewModelProvider;
import androidx.savedstate.SavedStateRegistryOwner;
import com.example.android.codelabs.paging.api.GithubService;
import com.example.android.codelabs.paging.data.GithubRepository;
import com.example.android.codelabs.paging.db.RepoDatabase;
import com.example.android.codelabs.paging.ui.ViewModelFactory;

public class Injection {

    private static GithubRepository provideGithubRepository(Context context) {
        return new GithubRepository(GithubService.create(), RepoDatabase.getInstance(context));
    }

    public static ViewModelProvider.Factory provideViewModelFactory(Context context, SavedStateRegistryOwner owner) {
        return new ViewModelFactory(owner, provideGithubRepository(context));
    }
}
