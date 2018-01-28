package com.isscroberto.onemovie.data.source.remote.retrofit;

import com.isscroberto.onemovie.data.models.Movie;
import com.isscroberto.onemovie.data.models.TmdbResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by isscr on 28/03/2017.
 */

public interface MovieApi {

    @GET("discover/movie")
    Call<TmdbResponse> discover(@Query("api_key") String apiKey,
                                @Query("sort_by") String sort_by,
                                @Query("page") int page,
                                @Query("primary_release_date.gte") String releaseGte,
                                @Query("primary_release_date.lte") String releaseLte,
                                @Query("with_release_type") String releaseType,
                                @Query("with_original_language") String language,
                                @Query("vote_average.gte") int vote,
                                @Query("with_genres") String genres);

    @GET("genre/movie/list")
    Call<TmdbResponse> getGenres(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getDetails(@Path("movie_id") String movieId, @Query("api_key") String apiKey);

}
