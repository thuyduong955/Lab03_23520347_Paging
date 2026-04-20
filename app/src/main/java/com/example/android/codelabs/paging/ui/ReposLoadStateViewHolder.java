package com.example.android.codelabs.paging.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.codelabs.paging.R;
import com.example.android.codelabs.paging.databinding.ReposLoadStateFooterViewItemBinding;

public class ReposLoadStateViewHolder extends RecyclerView.ViewHolder {
    private final ReposLoadStateFooterViewItemBinding binding;

    public ReposLoadStateViewHolder(ReposLoadStateFooterViewItemBinding binding, Runnable retry) {
        super(binding.getRoot());
        this.binding = binding;
        binding.retryButton.setOnClickListener(v -> retry.run());
    }

    public void bind(LoadState loadState) {
        if (loadState instanceof LoadState.Error) {
            binding.errorMsg.setText(((LoadState.Error) loadState).getError().getLocalizedMessage());
        }
        binding.progressBar.setVisibility(loadState instanceof LoadState.Loading ? View.VISIBLE : View.GONE);
        binding.retryButton.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
        binding.errorMsg.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
    }

    public static ReposLoadStateViewHolder create(ViewGroup parent, Runnable retry) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repos_load_state_footer_view_item, parent, false);
        ReposLoadStateFooterViewItemBinding binding = ReposLoadStateFooterViewItemBinding.bind(view);
        return new ReposLoadStateViewHolder(binding, retry);
    }
}
