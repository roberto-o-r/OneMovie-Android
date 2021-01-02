package com.isscroberto.onemovie.movie;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.stkent.amplify.prompt.DefaultLayoutPromptView;
import com.github.stkent.amplify.tracking.Amplify;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.isscroberto.onemovie.R;
import com.isscroberto.onemovie.data.models.Movie;
import com.isscroberto.onemovie.data.source.remote.MovieRemoteDataSource;
import com.isscroberto.onemovie.databinding.ActivityMovieBinding;
import com.isscroberto.onemovie.filter.FilterActivity;
import com.isscroberto.onemovie.settings.SettingsActivity;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {

    private MovieContract.Presenter presenter;
    private Movie movie;
    private ActivityMovieBinding binding;

    static final int SET_FILTER_REQUEST = 1;
    static final int RESULT_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding.
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.imageMovie.setOnClickListener((View v) -> imageMovieOnClick());
        binding.textMore.setOnClickListener((View v) -> textMoreOnClick());
        binding.buttonNext.setOnClickListener((View v) -> buttonNextOnClick());

        // Setup toolbar.
        setSupportActionBar(binding.toolbar);

        // Feedback.
        if (savedInstanceState == null) {
            DefaultLayoutPromptView promptView = (DefaultLayoutPromptView) findViewById(R.id.prompt_view);
            Amplify.getSharedInstance().promptIfReady(promptView);
        }

        // AdMob.
        MobileAds.initialize(this, initializationStatus -> {
        });
        setupAds();

        // Create the presenter
        presenter = new MoviePresenter(new MovieRemoteDataSource(), this, getSharedPreferences("com.isscroberto.onemovie", MODE_PRIVATE));
        presenter.takeView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.main, menu);

        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_filter) {
            navigateToFilter();
        } else if (id == R.id.menu_item_settings) {
            navigateToSettings();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_FILTER_REQUEST) {
            if (resultCode == RESULT_OK)
                // Reload movie.
                presenter.loadMovie();
        }
    }

    @Override
    public void showMovie(Movie movie) {
        this.movie = movie;
        if (movie.getPosterPath() != null) {
            Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()).fit().centerCrop().into(binding.imageMovie);
        } else {
            binding.imageMovie.setImageResource(R.drawable.logo_round);
        }
        binding.textTitle.setText(movie.getOriginalTitle());
        binding.textOverview.setText(movie.getOverview());
        binding.textReleaseDate.setText(movie.getReleaseDate());
        binding.textOriginalLanguage.setText(movie.getOriginalLanguage());
        binding.textPopularity.setText(String.valueOf(movie.getPopularity()));
        binding.textVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
    }

    @Override
    public void showPoster(String url) {
        Intent intent = new Intent(this, MoviePhotoActivity.class);
        intent.putExtra("Url", url);
        startActivity(intent);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            binding.layoutProgress.setVisibility(View.VISIBLE);
            binding.buttonNext.setVisibility(View.INVISIBLE);
        } else {
            binding.layoutProgress.setVisibility(View.GONE);
            binding.buttonNext.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void imageMovieOnClick() {
        // Show poster on browser due to copyright problems.
        if (movie.getPosterPath() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()));
            startActivity(intent);
        }
    }

    public void textMoreOnClick() {
        if (movie.getImdb_id() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/" + movie.getImdb_id()));
            startActivity(intent);
        }
    }

    public void buttonNextOnClick() {
        presenter.loadMovie();
    }

    private void navigateToFilter() {
        // Settings.
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, SET_FILTER_REQUEST);
    }

    private void navigateToSettings() {
        // Settings.
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void setupAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        binding.adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                binding.adWrapper.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                binding.adWrapper.setVisibility(View.GONE);
            }
        });
    }
}
