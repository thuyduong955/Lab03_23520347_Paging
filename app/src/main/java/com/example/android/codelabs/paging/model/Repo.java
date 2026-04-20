package com.example.android.codelabs.paging.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "repos")
public class Repo {
    @PrimaryKey
    @SerializedName("id")
    private final long id;
    @SerializedName("name")
    private final String name;
    @SerializedName("full_name")
    private final String fullName;
    @SerializedName("description")
    private final String description;
    @SerializedName("html_url")
    private final String url;
    @SerializedName("stargazers_count")
    private final int stars;
    @SerializedName("forks_count")
    private final int forks;
    @SerializedName("language")
    private final String language;

    public Repo(long id, String name, String fullName, String description, String url, int stars, int forks, String language) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.url = url;
        this.stars = stars;
        this.forks = forks;
        this.language = language;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getFullName() { return fullName; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
    public int getStars() { return stars; }
    public int getForks() { return forks; }
    public String getLanguage() { return language; }
}
