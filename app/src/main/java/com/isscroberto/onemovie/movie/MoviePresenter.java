package com.isscroberto.onemovie.movie;

import android.content.SharedPreferences;
import android.widget.Toast;

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

public class MoviePresenter implements MovieContract.Presenter  {

    private final MovieRemoteDataSource mMovieRemoteDataSource;
    private final MovieContract.View mMovieView;
    private Movie mMovie;
    private SharedPreferences mSharedPreferences;

    public MoviePresenter(MovieRemoteDataSource movieRemoteDataSource, MovieContract.View movieView, SharedPreferences sharedPreferences) {
        mMovieRemoteDataSource = movieRemoteDataSource;
        mMovieView = movieView;
        mSharedPreferences = sharedPreferences;

        mMovieView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMovie();
    }

    @Override
    public void loadMovie() {
        mMovieView.setLoadingIndicator(true);

        getRandomMovie(500);
    }

    @Override
    public void getPoster() {
        mMovieView.showPoster(mMovie.getPosterPath());
    }

    public void getRandomMovie(int maxPage) {
        // Load saved filter if exists.
        Filter filter  = new Filter();
        String json = mSharedPreferences.getString("Filter", "");
        if(!json.isEmpty()) {
            Gson gson = new Gson();
            filter = gson.fromJson(json, Filter.class);
        }
        filter.setMaxPage(maxPage);

        mMovieRemoteDataSource.getRandomResponse(new Callback<TmdbResponse>(){
            @Override
            public void onResponse(Call<TmdbResponse> call, Response<TmdbResponse> response) {
                // Verify if query contains results.
                if(response.body() != null && response.body().getTotal_results() > 0) {
                    // Verify if current page contains results.
                    if(response.body().getResults().size() == 0) {
                        // Try again with max page limit.
                        getRandomMovie(response.body().getTotal_pages());
                    } else {
                        // Get random number of movie.
                        Random r = new Random();
                        int index = r.nextInt(response.body().getResults().size());
                        mMovie = response.body().getResults().get(index);
                        // Get details of movie.
                        mMovieRemoteDataSource.getDetails(new Callback<Movie>() {
                            @Override
                            public void onResponse(Call<Movie> call, Response<Movie> response) {
                                mMovie = response.body();
                                mMovieView.showMovie(mMovie);
                                mMovieView.setLoadingIndicator(false);
                            }

                            @Override
                            public void onFailure(Call<Movie> call, Throwable t) {
                                mMovieView.showMessage("Sorry. An error has ocurred.");
                                mMovieView.setLoadingIndicator(false);
                            }
                        }, "" + mMovie.getId());
                    }
                } else {
                    mMovieView.showMessage("No movies found within the filter");
                    mMovieView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onFailure(Call<TmdbResponse> call, Throwable t) {
                mMovieView.setLoadingIndicator(false);
                mMovieView.showMessage("No network found");
            }
        }, filter);
    }

}
