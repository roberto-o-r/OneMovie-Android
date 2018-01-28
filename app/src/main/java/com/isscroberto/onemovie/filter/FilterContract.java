package com.isscroberto.onemovie.filter;

import com.isscroberto.onemovie.BasePresenter;
import com.isscroberto.onemovie.BaseView;
import com.isscroberto.onemovie.data.models.Filter;
import com.isscroberto.onemovie.data.models.Genre;
import com.isscroberto.onemovie.data.models.Language;

import java.util.List;
import java.util.Map;

/**
 * Created by roberto.orozco on 24/10/2017.
 */

public interface FilterContract {

    interface View extends BaseView<Presenter> {
        void showFilter(Filter filter);
        void showLanguages(List<Language> languages);
        void showYears(List<String> years);
        void showGenres(List<Genre> genres);
        void setLoadingIndicator(boolean active);
        void showError();
    }

    interface Presenter extends BasePresenter {
        void saveFilter(Filter filter);
        void getGenres();
    }

}
