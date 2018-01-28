package com.isscroberto.onemovie.data.source.remote;

import com.isscroberto.onemovie.data.models.Filter;
import com.isscroberto.onemovie.data.models.Movie;
import com.isscroberto.onemovie.data.models.TmdbResponse;
import com.isscroberto.onemovie.data.source.MovieDataSource;
import com.isscroberto.onemovie.data.source.remote.retrofit.MovieApi;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by isscr on 27/03/2017.
 */

public class MovieRemoteDataSource implements MovieDataSource {

    @Override
    public void getRandomResponse(final Callback<TmdbResponse> callback, Filter filter) {
        // Apply filter.
        int page = 0;
        String releaseLte = "";
        String releaseGte = "";
        String releaseType = "";
        String language = "";
        String genres = "";
        int vote = 0;
        // Get random page.
        Random r = new Random();
        page = r.nextInt(filter.getMaxPage()) + 1;
        // In Theatres.
        if (filter.isInTheatres()) {
            Calendar cal = Calendar.getInstance();
            String month = "";

            month = "" + (cal.get(Calendar.MONTH) + 1);
            if (month.length() == 1) month = "0" + month;
            releaseLte = cal.get(Calendar.YEAR) + "-" + month + "-" + cal.get(Calendar.DAY_OF_MONTH);

            cal.add(Calendar.MONTH, -1);
            month = "" + (cal.get(Calendar.MONTH) + 1);
            if (month.length() == 1) month = "0" + month;
            releaseGte = cal.get(Calendar.YEAR) + "-" + month + "-" + cal.get(Calendar.DAY_OF_MONTH);

            releaseType = "2|3";
        }
        // Language.
        if (filter.getLanguage() != null) {
            language = filter.getLanguage().getCode();
        }
        // Year.
        if (filter.getYear() != null && !filter.getYear().equals("All")) {
            releaseGte = filter.getYear() + "-01-01";
        }
        // Popular.
        if(filter.isPopular()) {
            if(page > 3) {
                page = r.nextInt(3) + 1;
            }
        }
        // Vote.
        if(filter.getVote() > 0) {
            vote = filter.getVote();
        }
        // Genre.
        if(filter.getGenre() != null && filter.getGenre().getId() > 0) {
            genres = "" + filter.getGenre().getId();
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();
        final MovieApi api = retrofit.create(MovieApi.class);
        Call<TmdbResponse> apiCall = api.discover("e0a913acbe8f949423ef68d754568944", "popularity.desc", page, releaseGte, releaseLte, releaseType, language, vote, genres);
        apiCall.enqueue(callback);
    }

    @Override
    public void getDetails(Callback<Movie> callback, String id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();
        final MovieApi api = retrofit.create(MovieApi.class);
        Call<Movie> apiCall = api.getDetails(id, "e0a913acbe8f949423ef68d754568944");
        apiCall.enqueue(callback);
    }

    @Override
    public void getGenres(Callback<TmdbResponse> callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").addConverterFactory(GsonConverterFactory.create()).build();
        final MovieApi api = retrofit.create(MovieApi.class);
        Call<TmdbResponse> apiCall = api.getGenres("e0a913acbe8f949423ef68d754568944");
        apiCall.enqueue(callback);
    }

}
