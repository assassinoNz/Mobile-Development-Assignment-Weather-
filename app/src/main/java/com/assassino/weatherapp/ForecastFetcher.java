package com.assassino.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class ForecastFetcher extends AsyncTask<String, Void, ForecastResponse> {
    private MainActivity mainActivity;
    private String latestUrl;

    ForecastFetcher(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        String city = PreferenceManager.getDefaultSharedPreferences(this.mainActivity).getString("keyCity", "Colombo");
        String units = PreferenceManager.getDefaultSharedPreferences(this.mainActivity).getString("keyTempUnit", "metric");
        this.latestUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=" + units + "&appid=2a29e6cec1c53b112ece0c7873415b29";
    }

    @Override
    protected ForecastResponse doInBackground(String... args) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            urlConnection = (HttpURLConnection) new URL(this.latestUrl).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                return null;
            } else {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                } else {
                    return new Gson().fromJson(buffer.toString(), ForecastResponse.class);
                }
            }
        } catch (IOException e) {
            Log.e("WEATHER_APP", e.getMessage());
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("WEATHER_APP", e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onPostExecute(ForecastResponse res) {
        super.onPostExecute(res);

        WeatherForecastArrayAdapter arrayAdapter = new WeatherForecastArrayAdapter(this.mainActivity, res.list);
        this.mainActivity.populateList(this.latestUrl, arrayAdapter);
    }
}
