package com.isscroberto.onemovie.settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.isscroberto.onemovie.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Bind views with Butter Knife.
        ButterKnife.bind(this);

        // Setup toolbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

        return true;
    }

    @OnClick(R.id.image_daily_prayer)
    public void imageDailyBibleOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.isscroberto.dailyprayerandroid")));
    }

    @OnClick(R.id.image_daily_bible)
    public void imageOneMovieOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.isscroberto.dailybibleandroid")));
    }

    @OnClick(R.id.image_daily_reflection)
    public void imageDailyReflectionOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.isscroberto.dailyreflectionandroid")));
    }

    @OnClick(R.id.image_one_breath)
    public void imageOneBreathOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.isscroberto.onebreath")));
    }

    @OnClick(R.id.text_privacy_policy)
    public void textPrivacyPolicy(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://isscroberto.com/daily-bible-privacy-policy/")));
    }
}
