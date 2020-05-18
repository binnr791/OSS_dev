package com.example.weatherforecastapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.weatherforecastapplication.WeatherDataManager;


//컴퓨터공학과 2019026071 빈현우
//This code has an error, NetWorkOnMainException
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = findViewById(R.id.lvOfFragment);
        ArrayList<String> sampleData = new ArrayList<>();

        sampleData.add("Sample Data1");
        sampleData.add("Sample Data2");
        sampleData.add("Sample Data3");

        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this,
                R.layout.list_view_item,
                R.id.list_item_forecast_textview, sampleData);
        listview.setAdapter(sAdapter);

        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent toDetailActv = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(toDetailActv);
            }
        };

        listview.setOnItemClickListener(clickListener);

        WeatherDataManager weatherDataManager = new WeatherDataManager();
        String rawData = weatherDataManager.getRawWeatherData();
        Log.i("RawWeatherData", rawData);
    }
}

