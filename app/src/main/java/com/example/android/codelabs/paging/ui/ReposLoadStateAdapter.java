package com.example.android.codelabs.paging.ui;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;

public class ReposLoadStateAdapter extends LoadStateAdapter<ReposLoadStateViewHolder> {
    private final Runnable retry;

    public ReposLoadStateAdapter(Runnable retry) {
        this.retry = retry;
    }

    @Override
    public void onBindViewHolder(@NonNull ReposLoadStateViewHolder holder, @NonNull LoadState loadState) {
        holder.bind(loadState);
    }

    @NonNull
    @Override
    public ReposLoadStateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @NonNull LoadState loadState) {
        return ReposLoadStateViewHolder.create(parent, retry);
    }
}
