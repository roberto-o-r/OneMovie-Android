package com.isscroberto.onemovie.settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.isscroberto.onemovie.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding.
        com.isscroberto.onemovie.databinding.ActivitySettingsBinding binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.imageDailyPrayer.setOnClickListener(this::imageDailyPrayerOnClick);
        binding.imageDailyBible.setOnClickListener(this::imageDailyBibleOnClick);
        binding.imageDailyReflection.setOnClickListener(this::imageDailyReflectionOnClick);
        binding.imageOneBreath.setOnClickListener(this::imageOneBreathOnClick);
        binding.textPrivacyPolicy.setOnClickListener(this::textPrivacyPolicyOnClick);

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

    public void imageDailyPrayerOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.isscroberto.dailyprayerandroid")));
    }

    public void imageDailyBibleOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.isscroberto.dailybibleandroid")));
    }

    public void imageDailyReflectionOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.isscroberto.dailyreflectionandroid")));
    }

    public void imageOneBreathOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.isscroberto.onebreath")));
    }

    public void textPrivacyPolicyOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://isscroberto.com/daily-bible-privacy-policy/")));
    }
}
