package com.example.weatherforecastapplication;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

//Integer = city ID, String = info about weather
public class FetchWeatherTask extends AsyncTask<Integer, String, String> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
    private ListView listView;
    private int cityID;
    public FetchWeatherTask(int cityID, View listViewIn)
    {
        this.listView =  (ListView) listViewIn;
        this.cityID = cityID;
    }

    @Override protected void onPreExecute() {
        Log.d("onPreExecute", "successfully executed");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Integer... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonStr = null;

        try {
            String stringID = Integer.toString(cityID);
            String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?id=" +
                    stringID +
                    "&mode=json&units=metric&cnt=7";
            String apiKey = "&APPID=5fd2f2cde90c1533efb95b19c048a528";
            URL url = new URL(baseUrl.concat(apiKey));

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            forecastJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return forecastJsonStr;
    }
    @Override protected void onProgressUpdate(String... progress) {

    }

    @Override protected void onPostExecute(String result) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<String> adapterOfListView = (ArrayAdapter<String>) listView.getAdapter();

        try {
            JSONObject rawData = new JSONObject(result);
            JSONObject jsonCity = rawData.getJSONObject("city");
            String cityName = jsonCity.getString("name");
            adapterOfListView.add(cityName);
            Log.d("onPostExecute", "successfully finished");
        }
        catch(JSONException e)
        {
            Log.e("JSONException", "error");
        }
    }
}
