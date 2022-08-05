package com.assassino.weatherapp;

import java.io.Serializable;

public class ForecastResponse implements Serializable {
    int cnt;
    Forecast[] list;
}

class Forecast implements Serializable {
    long dt;
    Main main;
    Weather[] weather;
}

class Main implements Serializable {
    double temp;
    double humidity;
}

class Weather implements Serializable {
    int id;
    String main;
    String description;
    String icon;
}

