package com.learn.weatherapplication;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;


public class WeatherCardFragment extends Fragment {

    TextView tv_city;
    WeatherIcon weatherIcon;
    TextView tv_temperature;
    TextView tv_details;
    Bundle args;

    public static WeatherCardFragment newInstance(Bundle args){
        WeatherCardFragment fragment = new WeatherCardFragment();
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        args = getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.weather_card, container, false);
        tv_city =view.findViewById(R.id.city_name);
        tv_temperature = view.findViewById(R.id.city_temperature);
        weatherIcon = view.findViewById(R.id.weather_icon);
        tv_details =view.findViewById(R.id.details);

        tv_city.setText(args.getString("city"));

        String temp =  String.format(Locale.US, "%.0f %s", args.getDouble("temperature"), Html.fromHtml("&#8451;"));
        tv_temperature.setText(temp);

        tv_details.setText(args.getString("description"));

        weatherIcon.setWeatherIcon(args.getString("iconid"));

        return view;
    }


}