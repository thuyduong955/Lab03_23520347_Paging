package com.example.android.codelabs.paging.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.codelabs.paging.R;
import com.example.android.codelabs.paging.model.Repo;

public class RepoViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView description;
    private final TextView stars;
    private final TextView language;
    private final TextView forks;

    private Repo repo;

    public RepoViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.repo_name);
        description = view.findViewById(R.id.repo_description);
        stars = view.findViewById(R.id.repo_stars);
        language = view.findViewById(R.id.repo_language);
        forks = view.findViewById(R.id.repo_forks);

        view.setOnClickListener(v -> {
            if (repo != null && repo.getUrl() != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(repo.getUrl()));
                view.getContext().startActivity(intent);
            }
        });
    }

    public void bind(Repo repo) {
        if (repo == null) {
            name.setText(itemView.getResources().getString(R.string.loading));
            description.setVisibility(View.GONE);
            language.setVisibility(View.GONE);
            stars.setText(itemView.getResources().getString(R.string.unknown));
            forks.setText(itemView.getResources().getString(R.string.unknown));
        } else {
            showRepoData(repo);
        }
    }

    private void showRepoData(Repo repo) {
        this.repo = repo;
        name.setText(repo.getFullName());

        int descriptionVisibility = View.GONE;
        if (repo.getDescription() != null) {
            description.setText(repo.getDescription());
            descriptionVisibility = View.VISIBLE;
        }
        description.setVisibility(descriptionVisibility);

        stars.setText(String.valueOf(repo.getStars()));
        forks.setText(String.valueOf(repo.getForks()));

        int languageVisibility = View.GONE;
        if (repo.getLanguage() != null && !repo.getLanguage().isEmpty()) {
            language.setText(itemView.getContext().getResources().getString(R.string.language, repo.getLanguage()));
            languageVisibility = View.VISIBLE;
        }
        language.setVisibility(languageVisibility);
    }

    public static RepoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_view_item, parent, false);
        return new RepoViewHolder(view);
    }
}
