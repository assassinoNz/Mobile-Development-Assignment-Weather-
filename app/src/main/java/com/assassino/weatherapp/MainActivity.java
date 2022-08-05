package com.assassino.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String latestUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String city = PreferenceManager.getDefaultSharedPreferences(this).getString("keyCity", "Colombo");
        String units = PreferenceManager.getDefaultSharedPreferences(this).getString("keyTempUnit", "metric");
        String newUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=" + units + "&appid=2a29e6cec1c53b112ece0c7873415b29";

        if (!latestUrl.equals(newUrl)) {
            ForecastFetcher forecastFetcher = new ForecastFetcher(MainActivity.this);
            forecastFetcher.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSettings: {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.mnuLogOut: {
                mAuth.signOut();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.mnuAbout: {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void populateList(String latestUrl, WeatherForecastArrayAdapter arrayAdapter) {
        this.latestUrl = latestUrl;
        ListView lstWeatherItems = findViewById(R.id.lstWeatherItems);
        lstWeatherItems.setAdapter(arrayAdapter);
    }
}

