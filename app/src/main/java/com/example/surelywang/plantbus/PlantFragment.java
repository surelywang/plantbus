package com.example.surelywang.plantbus;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class PlantFragment extends Fragment {

    Context context;
    Fragment fragment;
    private FragmentManager fragmentManager;
    Handler handler;

    TextView detailsField;
    TextView currentTemperatureField;
    ImageView weatherImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        handler = new Handler();

        View rootView = inflater.inflate(R.layout.activity_plant_fragment, container, false);
        detailsField = (TextView)rootView.findViewById(R.id.textView31);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.textView32);
        weatherImage = (ImageView) rootView.findViewById(R.id.imageView34);

        updateWeatherData("Berkeley");

        return rootView;
    }

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            JSONArray weather = json.getJSONArray("weather");
            String status = weather.getJSONObject(0).getString("main");
            detailsField.setText(status);

            currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp"))+ " ℃");

            //set image
            if (status.equals("Clouds")) {
                weatherImage.setImageResource(R.drawable.cloud);
            } else  if (status.equals("Clear")) {
                weatherImage.setImageResource(R.drawable.sunny);
            } else if (status.equals("Rain")) {
                weatherImage.setImageResource(R.drawable.rain);
            } else if (status.equals("Snow")) {
                weatherImage.setImageResource(R.drawable.snow);
            }

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
}
