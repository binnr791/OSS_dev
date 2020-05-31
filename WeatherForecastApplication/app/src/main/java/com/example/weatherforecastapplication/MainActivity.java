package com.example.weatherforecastapplication;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

//2019026071 빈현우
public class MainActivity extends AppCompatActivity
{
    public static final int NUM_OF_CITY = 5;
    public static final int NUM_OF_INFO = 7;

    private String[] idList = new String[NUM_OF_CITY];
    private int[] currentDate = new int[3];
    private double[][] weatherData = new double[NUM_OF_CITY][NUM_OF_INFO];

    ArrayList<String> sampleData;
    ArrayAdapter<String> sAdapter;
    public static Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = this;

        initializeSharedPreference();
        initializeIdList(idList);
        final ListView listView = findViewById(R.id.lvOfFragment);
        getWeatherData(); // initialize weather data, equal to push refresh button

        sampleData = new ArrayList<>();
        sAdapter = new ArrayAdapter<>(this,
                R.layout.list_view_item,
                R.id.list_item_forecast_textview, sampleData);
        listView.setAdapter(sAdapter);
        initializeCityList(sAdapter);

        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intentToDetail = new Intent(MainActivity.this, DetailActivity.class);
                updateWeatherData(intentToDetail);
                String clickedItem = (String) parent.getAdapter().getItem(position);
                intentToDetail.putExtra("name", clickedItem);
                startActivity(intentToDetail);
            }
        };
        listView.setOnItemClickListener(clickListener);

        // refresh button & setting button
        Button.OnClickListener btnClickListener = new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch (view.getId())
                {
                    case R.id.refresh_button:
                        getWeatherData();
                        break;
                    case R.id.setting_button:
                        Intent intentToSettings = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intentToSettings);
                        break;
                }
            }
        };
        Button refreshBtn = findViewById(R.id.refresh_button);
        refreshBtn.setOnClickListener(btnClickListener);
        Button settingBtn = findViewById(R.id.setting_button);
        settingBtn.setOnClickListener(btnClickListener);
    }

    public void getWeatherData()
    {
        // automatically update weatherData
        int i = 0;
        for(String id : idList)
        {
            FetchWeatherTask weatherDataManager = new FetchWeatherTask(id, i, currentDate, weatherData);
            i += 1;
            weatherDataManager.execute();
        }
    }

    private void updateWeatherData(Intent intentToDetail)
    {
        SharedPreferences sharedPref = getSharedPreferences(
                "Setting", Context.MODE_PRIVATE);

        intentToDetail.putExtra("daytime", currentDate);

        for(int i = 0; i < weatherData.length; ++i) // idList.length = weatherData.length
        {
            intentToDetail.putExtra(sharedPref.getString("city_name" + i, "None"), weatherData[i]);
        }
        //        intentToDetail.putExtra("Seoul", weatherData[CityIdList.INDEX_SEOUL]);
        //        intentToDetail.putExtra("Busan", weatherData[CityIdList.INDEX_BUSAN]);
        //        intentToDetail.putExtra("Daegu", weatherData[CityIdList.INDEX_DAEGU]);
        //        intentToDetail.putExtra("Daejeon", weatherData[CityIdList.INDEX_DAEJEON]);
    }

    public void initializeIdList(String[] idList)
    {
        SharedPreferences sharedPref = getSharedPreferences(
                "Setting", Context.MODE_PRIVATE
        );

        for(int i = 0; i < idList.length; ++i)
        {
            idList[i] = sharedPref.getString("city_key" + i, "");
        }
//        idList[CityIdList.INDEX_SEOUL] = CityIdList.ID_SEOUL;
//        idList[CityIdList.INDEX_BUSAN] = CityIdList.ID_BUSAN;
//        idList[CityIdList.INDEX_DAEGU] = CityIdList.ID_DAEGU;
//        idList[CityIdList.INDEX_DAEJEON] = CityIdList.ID_DAEJEON;
    }

    public void initializeCityList(ArrayAdapter aa) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<String> sAdapter = aa;
        SharedPreferences sharedPref = getSharedPreferences(
                "Setting", Context.MODE_PRIVATE
        );
        for (int i = 0; i < idList.length; ++i)
        {
            sAdapter.add(sharedPref.getString("city_name" + i, "None")); ////
        }
    }

    public boolean removeCity(int index)
    {
        String foundItem = sAdapter.getItem(index);
        Log.d("removeCity", foundItem);
        if(foundItem == "None")
        {
            return false;
        }
        sAdapter.remove(sAdapter.getItem(index));
        sAdapter.notifyDataSetChanged();
        return true;
    }

    public void addCity(int index)
    {
        SharedPreferences sharedPref = getSharedPreferences(
                "Setting", Context.MODE_PRIVATE
        );
        Log.d("removeCity", sharedPref.getString("city_name" + index, "None"));
        sAdapter.insert(sharedPref.getString("city_name" + index, "None"), index);
        sAdapter.notifyDataSetChanged();
    }

    public void initializeSharedPreference()
    {
        SharedPreferences sharedPref = getSharedPreferences(
                "Setting", Context.MODE_PRIVATE);
        String check = sharedPref.getString("city_name0", "Default");
        SharedPreferences.Editor editor = sharedPref.edit();
        if(check.equals("Default"))
        {
            for(int i = 0; i < idList.length; ++i)
            {
                editor.putString("city_key" + i,
                        getResources().getStringArray(R.array.pref_city_list_values)[i]);
                editor.putString("city_name" + i,
                        getResources().getStringArray(R.array.pref_city_list_titles)[i]);
                String a = getResources().getStringArray(R.array.pref_city_list_titles)[i];
                idList[i] = sharedPref.getString("city_key" + i, "-1");
            }
            editor.apply();
        }
    }

}
