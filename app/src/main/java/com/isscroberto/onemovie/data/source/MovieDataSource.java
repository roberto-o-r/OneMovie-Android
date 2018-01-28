package com.isscroberto.onemovie.data.source;

import com.isscroberto.onemovie.data.models.Filter;
import com.isscroberto.onemovie.data.models.Movie;
import com.isscroberto.onemovie.data.models.TmdbResponse;

import retrofit2.Callback;

/**
 * Created by isscr on 27/03/2017.
 */

public interface MovieDataSource {

    void getRandomResponse(Callback<TmdbResponse> callback, Filter filter);

    void getDetails(Callback<Movie> callback, String id);

    void getGenres(Callback<TmdbResponse> callback);

}
