package com.isscroberto.onemovie.movie;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.chrisbanes.photoview.PhotoView;
import com.isscroberto.onemovie.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviePhotoActivity extends AppCompatActivity {

    @BindView(R.id.photo_movie)
    PhotoView photoMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_photo);

        // Bind views with Butter Knife.
        ButterKnife.bind(this);

        // Setup toolbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Poster");

        // Get url.
        Intent intent = getIntent();
        String url = intent.getStringExtra("Url");

        // Set url.
        Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + url).fit().centerInside().into(photoMovie);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();

        return true;
    }

}
