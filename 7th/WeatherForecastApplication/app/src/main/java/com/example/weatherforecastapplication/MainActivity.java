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


//2019026071 빈현우
//This code has an error, NetWorkOnMainException
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.lvOfFragment);
        ArrayList<String> sampleData = new ArrayList<>();

        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this,
                R.layout.list_view_item,
                R.id.list_item_forecast_textview, sampleData);
        listView.setAdapter(sAdapter);

        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent toDetailActv = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(toDetailActv);
            }
        };

        listView.setOnItemClickListener(clickListener);

        FetchWeatherTask weatherDataManager = new FetchWeatherTask(1835847, listView); // Seoul
        weatherDataManager.execute();
        FetchWeatherTask weatherDataManager2 = new FetchWeatherTask(1838519, listView); // Busan
        weatherDataManager2.execute();
        Log.i("RawWeatherData", "");
    }
}
