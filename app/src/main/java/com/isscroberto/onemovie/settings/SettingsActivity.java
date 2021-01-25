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
        binding.textMoreApps.setOnClickListener(this::textMoreAppsOnClick);
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

    public void textMoreAppsOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:isscroberto")));
    }

    public void textPrivacyPolicyOnClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://isscroberto.com/daily-bible-privacy-policy/")));
    }
}
