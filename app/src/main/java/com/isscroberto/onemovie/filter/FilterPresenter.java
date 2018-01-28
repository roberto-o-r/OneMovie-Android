package com.isscroberto.onemovie.filter;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.isscroberto.onemovie.data.models.Filter;
import com.isscroberto.onemovie.data.models.Language;
import com.isscroberto.onemovie.data.models.TmdbResponse;
import com.isscroberto.onemovie.data.source.remote.MovieRemoteDataSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by roberto.orozco on 24/10/2017.
 */

public class FilterPresenter implements FilterContract.Presenter {

    private final FilterContract.View mFilterView;
    private Filter mFilter;
    private SharedPreferences mSharedPreferences;
    private final MovieRemoteDataSource mMovieRemoteDataSource;
    private List<Language> languages = new ArrayList<Language>();
    private List<String> years = new ArrayList<>();

    public FilterPresenter(MovieRemoteDataSource movieRemoteDataSource, FilterContract.View filterView, SharedPreferences sharedPreferences) {
        mMovieRemoteDataSource = movieRemoteDataSource;
        mFilterView = filterView;
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
        for(int i = year; i > year - 100; i--) {
            years.add("" + i);
        }

        mFilterView.setPresenter(this);
    }

    @Override
    public void start() {
        // Set loading indicator.
        mFilterView.setLoadingIndicator(true);

        // Get genres from api.
        getGenres();

        // Show languages.
        mFilterView.showLanguages(languages);

        // Show years.
        mFilterView.showYears(years);

        // Load saved filter if exists.
        mFilter = new Filter();
        String json = mSharedPreferences.getString("Filter", "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            mFilter = gson.fromJson(json, Filter.class);
        }

        // Show filter in view.
        mFilterView.showFilter(mFilter);
    }

    @Override
    public void saveFilter(Filter filter) {
        // Save filter in shared preferences
        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(filter);
        prefsEditor.putString("Filter", json);
        prefsEditor.commit();
    }

    @Override
    public void getGenres() {
        mMovieRemoteDataSource.getGenres(new Callback<TmdbResponse>(){
            @Override
            public void onResponse(Call<TmdbResponse> call, Response<TmdbResponse> response) {
                mFilterView.showGenres(response.body().getGenres());
                mFilterView.setLoadingIndicator(false);
            }

            @Override
            public void onFailure(Call<TmdbResponse> call, Throwable t) {
                mFilterView.setLoadingIndicator(false);
                mFilterView.showError();
            }
        });
    }

}
