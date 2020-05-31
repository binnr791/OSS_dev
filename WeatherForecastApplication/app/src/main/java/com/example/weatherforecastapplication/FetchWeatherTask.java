package com.example.weatherforecastapplication;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
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
    public static final int TEMP_MORN = 0;
    public static final int TEMP_DAY = 1;
    public static final int TEMP_EVE = 2;
    public static final int TEMP_NIGHT = 3;

    private String cityID;
    private int cityIndex;
    private double[][] weatherData;
    private int[] currentDate;
    public FetchWeatherTask(String cityID, int cityIndex, int[] currentDate, double[][] weatherData)
    {
        this.cityID = cityID;
        this.cityIndex = cityIndex;
        this.weatherData = weatherData;
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
            String stringID = cityID;
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
        try {
            //extracting temperature data
            JSONObject rawData = new JSONObject(result);
            JSONArray infoList = rawData.getJSONArray("list");
            JSONObject dt = infoList.getJSONObject(0);
            JSONObject jsonTemp = dt.getJSONObject("temp");
            int currentUnixTime = dt.getInt("dt");
            currentDate = unixToDayTime(currentUnixTime);

            weatherData[cityIndex][TEMP_MORN] = jsonTemp.getDouble("morn");
            weatherData[cityIndex][TEMP_DAY] = jsonTemp.getDouble("day");
            weatherData[cityIndex][TEMP_EVE] = jsonTemp.getDouble("eve");
            weatherData[cityIndex][TEMP_NIGHT] = jsonTemp.getDouble("night");
            Log.d("onPostExecute", "successfully finished");
        }
        catch(JSONException e)
        {
            Log.e("JSONException", "error");
        }
    }

    private int[] unixToDayTime(int unixTime)
    {
        int[] date = new int[3];
        int currentYear = 1970 + (unixTime / 86400) / 365;
        int numOfDay = unixTime / 86400;
        int dayOfCurrentYear = numOfDay % 365;
        int currentMonth = dayOfCurrentYear / 30;
        int currentDay = dayOfCurrentYear % 31;

        date[0] = currentYear;
        date[1] = currentMonth;
        date[2] = currentDay;
        return date;
    }
}
