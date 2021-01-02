package com.isscroberto.onemovie.filter;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.isscroberto.onemovie.data.models.Filter;
import com.isscroberto.onemovie.data.models.Language;
import com.isscroberto.onemovie.data.models.TmdbResponse;
import com.isscroberto.onemovie.data.source.remote.MovieRemoteDataSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by roberto.orozco on 24/10/2017.
 */

public class FilterPresenter implements FilterContract.Presenter {

    private FilterContract.View mView;
    private final SharedPreferences mSharedPreferences;
    private final MovieRemoteDataSource mMovieRemoteDataSource;
    private final List<Language> languages = new ArrayList<>();
    private final List<String> years = new ArrayList<>();

    public FilterPresenter(MovieRemoteDataSource movieRemoteDataSource, FilterContract.View filterView, SharedPreferences sharedPreferences) {
        mMovieRemoteDataSource = movieRemoteDataSource;
        mView = filterView;
        mSharedPreferences = sharedPreferences;

        languages.add(new Language("All", ""));
        languages.add(new Language("English", "en"));
        languages.add(new Language("Spanish", "es"));
        languages.add(new Language("Chinese", "zh"));
        languages.add(new Language("French", "fr"));
        languages.add(new Language("Italian", "it"));
        languages.add(new Language("Hindi", "hi"));
        languages.add(new Language("German", "de"));
        languages.add(new Language("Portuguese", "pt"));
        languages.add(new Language("Russian", "ru"));
        languages.add(new Language("Japanese", "ja"));

        // Populate years.
        years.add("All");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = year; i > year - 100; i--) {
            years.add("" + i);
        }
    }

    @Override
    public void takeView(FilterContract.View view) {
        this.mView = view;

        // Set loading indicator.
        mView.setLoadingIndicator(true);

        // Get genres from api.
        getGenres();

        // Show languages.
        mView.showLanguages(languages);

        // Show years.
        mView.showYears(years);

        // Load saved filter if exists.
        Filter mFilter = new Filter();
        String json = mSharedPreferences.getString("Filter", "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            mFilter = gson.fromJson(json, Filter.class);
        }

        // Show filter in view.
        mView.showFilter(mFilter);
    }

    @Override
    public void dropView() {
        this.mView = null;
    }

    @Override
    public void saveFilter(Filter filter) {
        // Save filter in shared preferences
        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(filter);
        prefsEditor.putString("Filter", json);
        prefsEditor.apply();
    }

    @Override
    public void getGenres() {
        mMovieRemoteDataSource.getGenres(new Callback<TmdbResponse>() {
            @Override
            public void onResponse(@NonNull Call<TmdbResponse> call, @NonNull Response<TmdbResponse> response) {
                mView.showGenres(response.body().getGenres());
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onFailure(@NonNull Call<TmdbResponse> call, @NonNull Throwable t) {
                mView.setLoadingIndicator(false);
                mView.showError();
            }
        });
    }
}
