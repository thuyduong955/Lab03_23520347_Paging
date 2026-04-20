package com.example.android.codelabs.paging.api;

import com.example.android.codelabs.paging.model.Repo;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

public class RepoSearchResponse {
    @SerializedName("total_count")
    private final int total;
    @SerializedName("items")
    private final List<Repo> items;
    private final Integer nextPage;

    public RepoSearchResponse() {
        this.total = 0;
        this.items = Collections.emptyList();
        this.nextPage = null;
    }

    public RepoSearchResponse(int total, List<Repo> items, Integer nextPage) {
        this.total = total;
        this.items = items;
        this.nextPage = nextPage;
    }

    public int getTotal() {
        return total;
    }

    public List<Repo> getItems() {
        return items;
    }

    public Integer getNextPage() {
        return nextPage;
    }
}
