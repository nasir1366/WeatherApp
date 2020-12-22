package com.learn.weatherapplication.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.learn.weatherapplication.App;
import com.learn.weatherapplication.data.CityDbHelper;
import com.learn.weatherapplication.data.CityModel;
import com.learn.weatherapplication.data.WeatherData;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class APIManager{
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private ServiceAPI serviceAPI;
    private ServiceAPINoConverter serviceAPINoConverter;
    private Context context;

    public APIManager(Context context){
        this.context = context;

    }

    public String prepareUrl(){
        CityDbHelper dbhelper;
        List<CityModel> citylist;
        dbhelper = new CityDbHelper(context);
        citylist = dbhelper.getCities("selected = 1", null,null);

        StringBuilder sb = new StringBuilder("group?id=");
        for(int i= 0; i < citylist.size() ; i++){
            sb.append(String.valueOf(citylist.get(i).getId()));
            if(i < citylist.size() - 1){
                sb.append(",");
            }
        }
        sb.append("&units=metric");
        sb.append("&APPID=" + App.API_KEY);
        return sb.toString();
    }

    public interface ServiceAPI{

        @GET()
        Call<WeatherData> getWeatherData(@Url String url);

    }

    public ServiceAPI getAPIService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder().setLenient().create()
                        )
                )
                .build();

        serviceAPI = retrofit.create(ServiceAPI.class);

        return serviceAPI;
    }


    public interface ServiceAPINoConverter{

        @GET()
        Call<JSONObject> getWeatherData(@Url String url);

    }

    public ServiceAPINoConverter getAPIServiceNoConverter(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new MyConverter())
                .build();

        serviceAPINoConverter = retrofit.create(ServiceAPINoConverter.class);

        return serviceAPINoConverter;
    }


}
