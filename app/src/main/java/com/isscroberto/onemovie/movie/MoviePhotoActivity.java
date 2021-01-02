package com.isscroberto.onemovie.movie;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.isscroberto.onemovie.databinding.ActivityMoviePhotoBinding;
import com.squareup.picasso.Picasso;

public class MoviePhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding.
        com.isscroberto.onemovie.databinding.ActivityMoviePhotoBinding binding = ActivityMoviePhotoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Setup toolbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Poster");

        // Get url.
        Intent intent = getIntent();
        String url = intent.getStringExtra("Url");

        // Set url.
        Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + url).fit().centerInside().into(binding.photoMovie);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();

        return true;
    }

}
