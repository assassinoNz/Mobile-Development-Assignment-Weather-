package com.assassino.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Forecast forecast = (Forecast) getIntent().getSerializableExtra("FORECAST");

        TextView txtOutDate = findViewById(R.id.txtOutDate);
        Date date = new Date(forecast.dt * 1000);
        SimpleDateFormat timeStrFormat = new SimpleDateFormat("dd.MM.yyyy h:mm a", Locale.ENGLISH);
        timeStrFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        txtOutDate.setText(timeStrFormat.format(date));

        TextView txtOutCity = findViewById(R.id.txtOutCity);
        txtOutCity.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("keyCity", "Colombo"));

        ImageView imgIcon = findViewById(R.id.imgIcon);
        Picasso.get().load("https://openweathermap.org/img/w/" + forecast.weather[0].icon + ".png").into(imgIcon);

        TextView txtOutTemperature = findViewById(R.id.txtOutTemperature);
        String units = PreferenceManager.getDefaultSharedPreferences(this).getString("keyTempUnit", "metric");
        txtOutTemperature.setText(forecast.main.temp + (units.equals("metric")?" \u2103":" \u2109"));

        TextView txtOutWeather = findViewById(R.id.txtOutWeather);
        txtOutWeather.setText(forecast.weather[0].description);

        TextView txtOutHumidity = findViewById(R.id.txtOutHumidity);
        txtOutHumidity.setText("Humidity: " + forecast.main.humidity + "%");
    }
}