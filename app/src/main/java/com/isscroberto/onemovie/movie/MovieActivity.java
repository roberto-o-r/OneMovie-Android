package com.isscroberto.onemovie.movie;

import android.content.Intent;
import android.net.Uri;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stkent.amplify.prompt.DefaultLayoutPromptView;
import com.github.stkent.amplify.tracking.Amplify;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.isscroberto.onemovie.BuildConfig;
import com.isscroberto.onemovie.R;
import com.isscroberto.onemovie.data.models.Movie;
import com.isscroberto.onemovie.data.source.remote.MovieRemoteDataSource;
import com.isscroberto.onemovie.filter.FilterActivity;
import com.isscroberto.onemovie.settings.SettingsActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {

    // Bindings.
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.button_next)
    FloatingActionButton buttonNext;
    @BindView(R.id.image_movie)
    ImageView imageMovie;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_overview)
    TextView textOverview;
    @BindView(R.id.text_release_date)
    TextView textReleaseDate;
    @BindView(R.id.text_original_language)
    TextView textOriginalLanguage;
    @BindView(R.id.text_popularity)
    TextView textPopularity;
    @BindView(R.id.text_vote_average)
    TextView textVoteAverage;
    @BindView(R.id.layout_progress)
    RelativeLayout layoutProgress;
    @BindView(R.id.ad_view)
    AdView adView;

    private MovieContract.Presenter mPresenter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Movie movie;

    static final int SET_FILTER_REQUEST = 1;
    static final int RESULT_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Bind views with Butter Knife.
        ButterKnife.bind(this);

        // Setup toolbar.
        setSupportActionBar(toolbar);

        // Feedback.
        if (savedInstanceState == null) {
            DefaultLayoutPromptView promptView = (DefaultLayoutPromptView) findViewById(R.id.prompt_view);
            Amplify.getSharedInstance().promptIfReady(promptView);
        }

        // Load Ad Banner.
        AdRequest adRequest;
        if (BuildConfig.DEBUG) {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(getString(R.string.test_device))
                    .build();
        } else {
            adRequest = new AdRequest.Builder().build();
        }
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adView.setVisibility(View.GONE);
            }
        });

        // Firebase analytics.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Create the presenter
        new MoviePresenter(new MovieRemoteDataSource(), this, getSharedPreferences("com.isscroberto.onemovie", MODE_PRIVATE));
        mPresenter.start();
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
        switch (item.getItemId()) {
            case R.id.menu_item_filter:
                navigateToFilter();
                break;
            case R.id.menu_item_settings:
                navigateToSettings();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SET_FILTER_REQUEST) {
            if (resultCode == RESULT_OK)
                // Reload movie.
                mPresenter.start();
        }
    }

    @Override
    public void setPresenter(MovieContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMovie(Movie movie) {
        this.movie = movie;
        if(movie.getPosterPath() != null) {
            Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()).fit().centerCrop().into(imageMovie);
        } else {
            imageMovie.setImageResource(R.drawable.logo_round);
        }
        textTitle.setText(movie.getOriginalTitle());
        textOverview.setText(movie.getOverview());
        textReleaseDate.setText(movie.getReleaseDate());
        textOriginalLanguage.setText(movie.getOriginalLanguage());
        textPopularity.setText(String.valueOf(movie.getPopularity()));
        textVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
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
            layoutProgress.setVisibility(View.VISIBLE);
            buttonNext.setVisibility(View.INVISIBLE);
        } else {
            layoutProgress.setVisibility(View.GONE);
            buttonNext.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.image_movie)
    public void imageMovieOnClick() {
        // Show poster on browser due to copyright problems.
        if(movie.getPosterPath() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()));
            startActivity(intent);
        }
    }

    @OnClick(R.id.text_more)
    public void textMoreOnClick() {
        if(movie.getImdb_id() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/" + movie.getImdb_id()));
            startActivity(intent);
        }
    }

    @OnClick(R.id.button_next)
    public void buttonNextOnClick() {
        mPresenter.start();
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

}
