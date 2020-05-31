package com.example.weatherforecastapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView cityNameText = findViewById(R.id.city_name_text);
        TextView tempMornText = findViewById(R.id.morn_temp_text);
        TextView tempDayText = findViewById(R.id.day_temp_text);
        TextView tempEveText = findViewById(R.id.eve_temp_text);
        TextView tempNightText = findViewById(R.id.night_temp_text);

        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        double[] weatherData = intent.getDoubleArrayExtra(city);
        cityNameText.setText(city);
        tempMornText.setText("Morning : " + Double.toString(weatherData[FetchWeatherTask.TEMP_MORN]) + "ºC");
        tempDayText.setText("Day : " + Double.toString(weatherData[FetchWeatherTask.TEMP_DAY]) + "ºC");
        tempEveText.setText("Evening : " + Double.toString(weatherData[FetchWeatherTask.TEMP_EVE]) + "ºC");
        tempNightText.setText("Night : " + Double.toString(weatherData[FetchWeatherTask.TEMP_NIGHT]) + "ºC");
    }
}
