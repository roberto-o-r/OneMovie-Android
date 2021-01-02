package com.isscroberto.onemovie.movie;

import com.isscroberto.onemovie.BasePresenter;
import com.isscroberto.onemovie.BaseView;
import com.isscroberto.onemovie.data.models.Movie;

/**
 * Created by isscr on 27/03/2017.
 */

public interface MovieContract {

    interface View extends BaseView<Presenter> {
        void showMovie(Movie movie);
        void showPoster(String url);
        void setLoadingIndicator(boolean active);
        void showMessage(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void loadMovie();
        void getPoster();
    }

}
