package com.example.android.codelabs.paging.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubService {

    @GET("search/repositories?sort=stars")
    Call<RepoSearchResponse> searchRepos(
            @Query("q") String query,
            @Query("page") int page,
            @Query("per_page") int itemsPerPage
    );

    static GithubService create() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logger)
                .build();
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubService.class);
    }
}
