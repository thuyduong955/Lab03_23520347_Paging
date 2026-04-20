package com.example.android.codelabs.paging.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.DividerItemDecoration;
import com.example.android.codelabs.paging.Injection;
import com.example.android.codelabs.paging.databinding.ActivitySearchRepositoriesBinding;

public class SearchRepositoriesActivity extends AppCompatActivity {

    private ActivitySearchRepositoriesBinding binding;
    private SearchRepositoriesViewModel viewModel;
    private ReposAdapter adapter = new ReposAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchRepositoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, Injection.provideViewModelFactory(this, this))
                .get(SearchRepositoriesViewModel.class);

        binding.list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        initAdapter();
        initSearch();
        initList();
    }

    private void initAdapter() {
        binding.list.setAdapter(adapter.withLoadStateHeaderAndFooter(
                new ReposLoadStateAdapter(() -> adapter.retry()),
                new ReposLoadStateAdapter(() -> adapter.retry())
        ));
    }

    private void initSearch() {
        binding.searchRepo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput();
                return true;
            }
            return false;
        });
        binding.searchRepo.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput();
                return true;
            }
            return false;
        });

        viewModel.state.observe(this, uiState -> {
            if (!binding.searchRepo.getText().toString().equals(uiState.getQuery())) {
                binding.searchRepo.setText(uiState.getQuery());
            }
        });
    }

    private void updateRepoListFromInput() {
        String query = binding.searchRepo.getText().toString().trim();
        if (!query.isEmpty()) {
            binding.list.scrollToPosition(0);
        }
    }

    private void initList() {
        binding.retryButton.setOnClickListener(v -> adapter.retry());

        adapter.addLoadStateListener(loadStates -> {
            boolean isListEmpty = loadStates.getRefresh() instanceof LoadState.NotLoading && adapter.getItemCount() == 0;
            binding.emptyList.setVisibility(isListEmpty ? View.VISIBLE : View.GONE);
            binding.list.setVisibility(loadStates.getSource().getRefresh() instanceof LoadState.NotLoading ? View.VISIBLE : View.GONE);
            binding.progressBar.setVisibility(loadStates.getMediator() != null && loadStates.getMediator().getRefresh() instanceof LoadState.Loading ? View.VISIBLE : View.GONE);
            binding.retryButton.setVisibility(loadStates.getMediator() != null && loadStates.getMediator().getRefresh() instanceof LoadState.Error && adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

            LoadState errorState = null;
            if (loadStates.getSource().getAppend() instanceof LoadState.Error) {
                errorState = loadStates.getSource().getAppend();
            } else if (loadStates.getSource().getPrepend() instanceof LoadState.Error) {
                errorState = loadStates.getSource().getPrepend();
            } else if (loadStates.getAppend() instanceof LoadState.Error) {
                errorState = loadStates.getAppend();
            } else if (loadStates.getPrepend() instanceof LoadState.Error) {
                errorState = loadStates.getPrepend();
            }

            if (errorState instanceof LoadState.Error) {
                Toast.makeText(this, "\uD83D\uDE28 Wooops " + ((LoadState.Error) errorState).getError(), Toast.LENGTH_LONG).show();
            }
            return null;
        });
    }
}
