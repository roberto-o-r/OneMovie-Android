package com.isscroberto.onemovie.filter;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;
import com.isscroberto.onemovie.R;
import com.isscroberto.onemovie.data.models.Filter;
import com.isscroberto.onemovie.data.models.Genre;
import com.isscroberto.onemovie.data.models.Language;
import com.isscroberto.onemovie.data.source.remote.MovieRemoteDataSource;
import com.isscroberto.onemovie.movie.MovieContract;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class FilterActivity extends AppCompatActivity implements FilterContract.View {

    // Bindings.
    @BindView(R.id.switch_inTheatres)
    SwitchCompat switchInTheatres;
    @BindView(R.id.spinner_year)
    MaterialSpinner spinnerYear;
    @BindView(R.id.spinner_language)
    MaterialSpinner spinnerLanguage;
    @BindView(R.id.switch_popular)
    SwitchCompat switchPopular;
    @BindView(R.id.button_save)
    FloatingActionButton buttonSave;
    @BindView(R.id.range_vote)
    RangeBar rangeVote;
    @BindView(R.id.spinner_genre)
    MaterialSpinner spinnerGenre;
    @BindView(R.id.layout_progress)
    RelativeLayout layoutProgress;

    private FilterContract.Presenter mPresenter;
    private ArrayAdapter<Language> languageAdapter;
    private List<Language> languages;
    private ArrayAdapter<String> yearAdapter;
    private List<String> years;
    private ArrayAdapter<Genre> genresAdapter;
    private List<Genre> genres;
    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Bind views with Butter Knife.
        ButterKnife.bind(this);

        // Setup toolbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Language.
        spinnerLanguage.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<Language>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Language item) {

            }
        });

        // Create the presenter
        new FilterPresenter(new MovieRemoteDataSource(), this, getSharedPreferences("com.isscroberto.onemovie", MODE_PRIVATE));
        mPresenter.start();

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

        return true;
    }

    @Override
    public void setPresenter(FilterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showFilter(Filter filter) {
        this.filter = filter;
        // In Theatres.
        switchInTheatres.setChecked(filter.isInTheatres());
        // Language.
        if(filter.getLanguage() != null) {
            for(int i = 0; i < languages.size(); i++)
            {
                if(languages.get(i).getCode().equals(filter.getLanguage().getCode())) {
                    spinnerLanguage.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Year.
        if(filter.getLanguage() != null) {
            for(int i = 0; i < years.size(); i++)
            {
                if(years.get(i).equals(filter.getYear())) {
                    spinnerYear.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Popular.
        switchPopular.setChecked(filter.isPopular());
        // Vote.
        rangeVote.setSeekPinByValue(filter.getVote());
    }

    @Override
    public void showLanguages(List<Language> languages) {
        this.languages = languages;
        languageAdapter = new ArrayAdapter<Language>(this, android.R.layout.simple_spinner_item, this.languages);
        spinnerLanguage.setAdapter(languageAdapter);
    }

    @Override
    public void showYears(List<String> years) {
        this.years = years;
        yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.years);
        spinnerYear.setAdapter(yearAdapter);
    }

    @Override
    public void showGenres(List<Genre> genres) {
        this.genres = new ArrayList<Genre>();
        this.genres.add(new Genre(0, "All"));
        this.genres.addAll(genres);
        genresAdapter = new ArrayAdapter<Genre>(this, android.R.layout.simple_spinner_item, this.genres);
        spinnerGenre.setAdapter(genresAdapter);

        // Language.
        if(filter.getGenre() != null) {
            for(int i = 0; i < this.genres.size(); i++)
            {
                if(this.genres.get(i).getId() == filter.getGenre().getId()) {
                    spinnerGenre.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            layoutProgress.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.INVISIBLE);
        } else {
            layoutProgress.setVisibility(View.GONE);
            buttonSave.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError() {
        Toast.makeText(this, "No network found", Toast.LENGTH_LONG).show();
        this.finish();
    }

    private void saveFilter () {
        Filter filter = new Filter();
        // In theatres.
        filter.setInTheatres(switchInTheatres.isChecked());
        // Language.
        filter.setLanguage(languageAdapter.getItem(spinnerLanguage.getSelectedIndex()));
        // Year.
        filter.setYear(yearAdapter.getItem(spinnerYear.getSelectedIndex()));
        // Popularity.
        filter.setPopular(switchPopular.isChecked());
        // Vote.
        filter.setVote(rangeVote.getRightIndex());
        // Genre.
        filter.setGenre(genresAdapter.getItem(spinnerGenre.getSelectedIndex()));

        mPresenter.saveFilter(filter);

        setResult(200);
        this.finish();
    }

    @OnClick(R.id.button_save)
    public void buttonSaveOnClick() {
        saveFilter();
    }

    @OnCheckedChanged(R.id.switch_inTheatres)
    public void switchInTheatresOnCheckedChanged() {
        if(switchInTheatres.isChecked()) {
            spinnerYear.setSelectedIndex(0);
            spinnerYear.setEnabled(false);
        } else {
            spinnerYear.setSelectedIndex(0);
            spinnerYear.setEnabled(true);
        }
    }

}
