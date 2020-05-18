package com.example.weatherforecastapplication;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ForecastFragment extends Fragment {

    private ArrayList<String> SamepleDataArray;
    private ListView listView;

    public ForecastFragment() {
        SamepleDataArray = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.forecast_fragment, container, false);
    }
}