package com.isscroberto.onemovie.movie;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.isscroberto.onemovie.data.models.Filter;
import com.isscroberto.onemovie.data.models.Movie;
import com.isscroberto.onemovie.data.models.TmdbResponse;
import com.isscroberto.onemovie.data.source.remote.MovieRemoteDataSource;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by isscr on 27/03/2017.
 */

public class MoviePresenter implements MovieContract.Presenter {

    private final MovieRemoteDataSource movieRemoteDataSource;
    private MovieContract.View view;
    private Movie movie;
    private final SharedPreferences sharedPreferences;

    public MoviePresenter(MovieRemoteDataSource movieRemoteDataSource, MovieContract.View movieView, SharedPreferences sharedPreferences) {
        this.movieRemoteDataSource = movieRemoteDataSource;
        view = movieView;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void takeView(MovieContract.View movieView) {
        this.view = movieView;
        loadMovie();
    }

    @Override
    public void dropView() {

    }

    @Override
    public void loadMovie() {
        view.setLoadingIndicator(true);

        getRandomMovie(500);
    }

    @Override
    public void getPoster() {
        view.showPoster(movie.getPosterPath());
    }

    public void getRandomMovie(int maxPage) {
        // Load saved filter if exists.
        Filter filter = new Filter();
        String json = sharedPreferences.getString("Filter", "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            filter = gson.fromJson(json, Filter.class);
        }
        filter.setMaxPage(maxPage);

        movieRemoteDataSource.getRandomResponse(new Callback<TmdbResponse>() {
            @Override
            public void onResponse(@NonNull Call<TmdbResponse> call, @NonNull Response<TmdbResponse> response) {
                // Verify if query contains results.
                if (response.body() != null && response.body().getTotal_results() > 0) {
                    // Verify if current page contains results.
                    if (response.body().getResults().size() == 0) {
                        // Try again with max page limit.
                        getRandomMovie(response.body().getTotal_pages());
                    } else {
                        // Get random number of movie.
                        Random r = new Random();
                        int index = r.nextInt(response.body().getResults().size());
                        movie = response.body().getResults().get(index);
                        // Get details of movie.
                        movieRemoteDataSource.getDetails(new Callback<Movie>() {
                            @Override
                            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                                movie = response.body();
                                view.showMovie(movie);
                                view.setLoadingIndicator(false);
                            }

                            @Override
                            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                                view.showMessage("Sorry. An error has ocurred.");
                                view.setLoadingIndicator(false);
                            }
                        }, "" + movie.getId());
                    }
                } else {
                    view.showMessage("No movies found within the filter");
                    view.setLoadingIndicator(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TmdbResponse> call, @NonNull Throwable t) {
                view.setLoadingIndicator(false);
                view.showMessage("No network found");
            }
        }, filter);
    }
}
