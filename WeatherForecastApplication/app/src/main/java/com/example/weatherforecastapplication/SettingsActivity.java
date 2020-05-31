package com.example.weatherforecastapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ListPreference[] lpList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        final MainActivity mainConext = (MainActivity) MainActivity.mainContext;

        sharedPref = getSharedPreferences(
                "Setting", Context.MODE_PRIVATE
        );
        editor = sharedPref.edit();
        lpList = new ListPreference[MainActivity.NUM_OF_CITY];

        Preference.OnPreferenceChangeListener changeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object keyOfSelectedItem) {
                for(int i = 0 ; i < lpList.length; ++i)
                {
                    if(preference == lpList[i])
                    {
                        String stringKey = (String) keyOfSelectedItem;

                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringKey);
                        preference.setSummary(index >= 0
                                ? listPreference.getEntries()[index] : null);
                        editor.putString("city_key" + i, stringKey);
                        editor.putString("city_name" + i, listPreference.getEntries()[index].toString());
                        editor.apply();

                        mainConext.removeCity(i);
                        mainConext.addCity(i);
                        mainConext.getWeatherData();

                        return true;
                    }
                }
                return false;
            }
        };

        for(int i = 0 ; i < lpList.length; ++i)
        {
            lpList[i] = (ListPreference) findPreference("city_list_pref" + i);
            lpList[i].setOnPreferenceChangeListener(changeListener);
            int index = lpList[i].findIndexOfValue(sharedPref.getString("city_key" + i, "-1"));
            lpList[i].setSummary(index >= 0
                    ? lpList[i].getEntries()[index] : null);
        }

    }
}
