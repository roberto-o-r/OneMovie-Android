package com.isscroberto.onemovie.filter;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.isscroberto.onemovie.data.models.Filter;
import com.isscroberto.onemovie.data.models.Genre;
import com.isscroberto.onemovie.data.models.Language;
import com.isscroberto.onemovie.data.source.remote.MovieRemoteDataSource;
import com.isscroberto.onemovie.databinding.ActivityFilterBinding;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements FilterContract.View {

    private FilterContract.Presenter presenter;
    private ArrayAdapter<Language> languageAdapter;
    private List<Language> languages;
    private ArrayAdapter<String> yearAdapter;
    private List<String> years;
    private ArrayAdapter<Genre> genresAdapter;
    private Filter filter;
    private ActivityFilterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding.
        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.buttonSave.setOnClickListener((View v) -> saveFilter());
        binding.switchInTheatres.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> switchInTheatresOnCheckedChanged());

        // Setup toolbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Language.
        binding.spinnerLanguage.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<Language>) (view1, position, id, item) -> {
        });

        // Create the presenter.
        presenter = new FilterPresenter(new MovieRemoteDataSource(), this, getSharedPreferences("com.isscroberto.onemovie", MODE_PRIVATE));
        presenter.takeView(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

        return true;
    }

    @Override
    public void showFilter(Filter filter) {
        this.filter = filter;
        // In Theatres.
        binding.switchInTheatres.setChecked(filter.isInTheatres());
        // Language.
        if (filter.getLanguage() != null) {
            for (int i = 0; i < languages.size(); i++) {
                if (languages.get(i).getCode().equals(filter.getLanguage().getCode())) {
                    binding.spinnerLanguage.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Year.
        if (filter.getLanguage() != null) {
            for (int i = 0; i < years.size(); i++) {
                if (years.get(i).equals(filter.getYear())) {
                    binding.spinnerYear.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Popular.
        binding.switchPopular.setChecked(filter.isPopular());
        // Vote.
        binding.rangeVote.setSeekPinByValue(filter.getVote());
    }

    @Override
    public void showLanguages(List<Language> languages) {
        this.languages = languages;
        languageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, this.languages);
        binding.spinnerLanguage.setAdapter(languageAdapter);
    }

    @Override
    public void showYears(List<String> years) {
        this.years = years;
        yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, this.years);
        binding.spinnerYear.setAdapter(yearAdapter);
    }

    @Override
    public void showGenres(List<Genre> genres) {
        List<Genre> genres1 = new ArrayList<>();
        genres1.add(new Genre(0, "All"));
        genres1.addAll(genres);
        genresAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genres1);
        binding.spinnerGenre.setAdapter(genresAdapter);

        // Language.
        if (filter.getGenre() != null) {
            for (int i = 0; i < genres1.size(); i++) {
                if (genres1.get(i).getId() == filter.getGenre().getId()) {
                    binding.spinnerGenre.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            binding.layoutProgress.setVisibility(View.VISIBLE);
            binding.buttonSave.setVisibility(View.INVISIBLE);
        } else {
            binding.layoutProgress.setVisibility(View.GONE);
            binding.buttonSave.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError() {
        Toast.makeText(this, "No network found", Toast.LENGTH_LONG).show();
        this.finish();
    }

    private void saveFilter() {
        Filter filter = new Filter();
        // In theatres.
        filter.setInTheatres(binding.switchInTheatres.isChecked());
        // Language.
        filter.setLanguage(languageAdapter.getItem(binding.spinnerLanguage.getSelectedIndex()));
        // Year.
        filter.setYear(yearAdapter.getItem(binding.spinnerYear.getSelectedIndex()));
        // Popularity.
        filter.setPopular(binding.switchPopular.isChecked());
        // Vote.
        filter.setVote(binding.rangeVote.getRightIndex());
        // Genre.
        filter.setGenre(genresAdapter.getItem(binding.spinnerGenre.getSelectedIndex()));

        presenter.saveFilter(filter);

        setResult(200);
        this.finish();
    }

    private void switchInTheatresOnCheckedChanged() {
        if (binding.switchInTheatres.isChecked()) {
            binding.spinnerYear.setSelectedIndex(0);
            binding.spinnerYear.setEnabled(false);
        } else {
            binding.spinnerYear.setSelectedIndex(0);
            binding.spinnerYear.setEnabled(true);
        }
    }

}
