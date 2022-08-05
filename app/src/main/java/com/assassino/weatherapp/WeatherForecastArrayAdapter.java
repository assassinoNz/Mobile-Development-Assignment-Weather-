package com.assassino.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherForecastArrayAdapter extends ArrayAdapter<Forecast> {
    Activity mainActivity;

    public WeatherForecastArrayAdapter(Activity mainActivity, Forecast[] forecasts) {
        super(mainActivity, R.layout.list_item, forecasts);
        this.mainActivity = mainActivity;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        View lstItem = mainActivity.getLayoutInflater().inflate(R.layout.list_item, null, true);

        ImageView imgIcon = lstItem.findViewById(R.id.imgIcon);
        Picasso.get().load("https://openweathermap.org/img/w/" + getItem(pos).weather[0].icon + ".png").into(imgIcon);

        Date date = new Date(getItem(pos).dt * 1000);
        SimpleDateFormat dateStrFormat = new SimpleDateFormat("EEEE, d", Locale.ENGLISH);
        SimpleDateFormat timeStrFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        dateStrFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeStrFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        TextView txtOutDay = lstItem.findViewById(R.id.txtOutDay);
        txtOutDay.setText(dateStrFormat.format(date));

        TextView txtOutWeather = lstItem.findViewById(R.id.txtOutWeather);
        txtOutWeather.setText(getItem(pos).weather[0].main + " at " + timeStrFormat.format(date));

        TextView txtOutTemperature = lstItem.findViewById(R.id.txtOutTemperature);
        String units = PreferenceManager.getDefaultSharedPreferences(mainActivity).getString("keyTempUnit", "metric");
        txtOutTemperature.setText(getItem(pos).main.temp + (units.equals("metric")?" \u2103":" \u2109"));

        lstItem.setOnClickListener(lstItemView -> {
            Intent intent = new Intent(mainActivity, DetailsActivity.class);
            intent.putExtra("FORECAST", getItem(pos));
            mainActivity.startActivity(intent);
        });

        return lstItem;
    }
}



