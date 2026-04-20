package com.example.android.codelabs.paging.ui;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.codelabs.paging.R;

public class ReposAdapter extends PagingDataAdapter<SearchRepositoriesViewModel.UiModel, RecyclerView.ViewHolder> {

    public ReposAdapter() {
        super(UIMODEL_COMPARATOR);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == R.layout.repo_view_item) {
            return RepoViewHolder.create(parent);
        } else {
            return SeparatorViewHolder.create(parent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        SearchRepositoriesViewModel.UiModel item = getItem(position);
        if (item instanceof SearchRepositoriesViewModel.UiModel.RepoItem) {
            return R.layout.repo_view_item;
        } else if (item instanceof SearchRepositoriesViewModel.UiModel.SeparatorItem) {
            return R.layout.separator_view_item;
        } else {
            throw new UnsupportedOperationException("Unknown view");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchRepositoriesViewModel.UiModel uiModel = getItem(position);
        if (uiModel instanceof SearchRepositoriesViewModel.UiModel.RepoItem) {
            ((RepoViewHolder) holder).bind(((SearchRepositoriesViewModel.UiModel.RepoItem) uiModel).getRepo());
        } else if (uiModel instanceof SearchRepositoriesViewModel.UiModel.SeparatorItem) {
            ((SeparatorViewHolder) holder).bind(((SearchRepositoriesViewModel.UiModel.SeparatorItem) uiModel).getDescription());
        }
    }

    private static final DiffUtil.ItemCallback<SearchRepositoriesViewModel.UiModel> UIMODEL_COMPARATOR = new DiffUtil.ItemCallback<SearchRepositoriesViewModel.UiModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull SearchRepositoriesViewModel.UiModel oldItem, @NonNull SearchRepositoriesViewModel.UiModel newItem) {
            boolean isSameRepo = oldItem instanceof SearchRepositoriesViewModel.UiModel.RepoItem && newItem instanceof SearchRepositoriesViewModel.UiModel.RepoItem &&
                    ((SearchRepositoriesViewModel.UiModel.RepoItem) oldItem).getRepo().getFullName().equals(((SearchRepositoriesViewModel.UiModel.RepoItem) newItem).getRepo().getFullName());
            boolean isSameSeparator = oldItem instanceof SearchRepositoriesViewModel.UiModel.SeparatorItem && newItem instanceof SearchRepositoriesViewModel.UiModel.SeparatorItem &&
                    ((SearchRepositoriesViewModel.UiModel.SeparatorItem) oldItem).getDescription().equals(((SearchRepositoriesViewModel.UiModel.SeparatorItem) newItem).getDescription());
            return isSameRepo || isSameSeparator;
        }

        @Override
        public boolean areContentsTheSame(@NonNull SearchRepositoriesViewModel.UiModel oldItem, @NonNull SearchRepositoriesViewModel.UiModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
