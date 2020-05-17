package com.example.weatherforecastapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        ForecastFragment forecastFragment = new ForecastFragment();
        fragmentTransaction.add(R.id.main_view, forecastFragment);
        fragmentTransaction.commit();

        ListView listview = findViewById(R.id.lvOfFragment);
        ArrayList<String> sampleData = new ArrayList<>();

        sampleData.add("Sample Data1");
        sampleData.add("Sample Data2");
        sampleData.add("Sample Data3");

        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this,
                R.layout.list_view_item,
                R.id.list_item_forecast_textview, sampleData);
        listview.setAdapter(sAdapter);
    }
}
