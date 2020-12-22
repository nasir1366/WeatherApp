package com.learn.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.learn.weatherapplication.data.CityDbHelper;
import com.learn.weatherapplication.data.CityModel;
import com.learn.weatherapplication.data.CityWeatherInfo;
import com.learn.weatherapplication.data.WeatherData;
import com.learn.weatherapplication.retrofit.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    CityDbHelper dbhelper;
    JsonObjectRequest request;
    List<CityModel> citylist;
    ProgressBar pb;
    MyPagerAdapter adapter;
    ViewPager2 viewPager;
    APIManager apiManager;

//    FragmentTransaction transaction;
//    private int index;

//    Handler updateHandler;
//    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb =  findViewById(R.id.progress_bar);
        pb.setVisibility(View.INVISIBLE);

        init();
//        updateHandler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                loadWeatherData();
//                updateHandler.postDelayed(this, 5 * 1000);
//            }
//        };
    }

    @Override
    protected void onResume() {
        Log.i("weather", "onResume");
        super.onResume();
//        loadWeatherData();
        loadWeatherDataRetrofit();
//        updateHandler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        updateHandler.removeCallbacks(runnable);
    }

    private void init() {
        dbhelper = new CityDbHelper(this);
        citylist = dbhelper.getCities("selected = 1", null,null);
        viewPager = findViewById(R.id.view_pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager.setAdapter(adapter);
        apiManager = new APIManager(this);
    }

    private void loadWeatherData() {
        citylist = dbhelper.getCities("selected = 1", null,null);
        String url = apiManager.prepareUrl();
        request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pb.setVisibility(View.INVISIBLE);
                Log.i("weather", "response : \n" + response);
                //clear fragment list
                adapter = new MyPagerAdapter(getSupportFragmentManager(),getLifecycle());
                viewPager.setAdapter(adapter);
                parseWeatherJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("weather", "error : " + error.getMessage());
                pb.setVisibility(View.INVISIBLE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        pb.setVisibility(View.VISIBLE);

    }

    public void parseWeatherJson(JSONObject response) {
        try {
            int cnt = response.getInt("cnt");
            if(cnt == 0) return;
            JSONArray jsonlist = response.getJSONArray("list");

            for(int i = 0; i < jsonlist.length() ; i++){
                JSONObject res = jsonlist.getJSONObject(i);
                String cityname = res.getString("name").toUpperCase() + ", " +
                        res.getJSONObject("sys").getString("country");

                double temprature = res.getJSONObject("main").getDouble("temp");
                JSONObject  jsondetails = res.getJSONArray("weather").getJSONObject(0);
                String details = jsondetails.getString("description");
                String weatherId = jsondetails.getString("icon");

                Bundle args = new Bundle();
                args.putString("city", cityname);
                args.putDouble("temperature", temprature);
                args.putString("iconid", weatherId);
                args.putString("description", details);
                //add new fragment to list
                adapter.add(args);
            }
            updateDisplay();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadWeatherDataRetrofit(){
        pb.setVisibility(View.VISIBLE);

        String url = apiManager.prepareUrl();
        Log.i("retrofit" , "url : "+ url);

        Call<WeatherData> call = apiManager.getAPIService().getWeatherData(url);

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, retrofit2.Response<WeatherData> response) {
                Log.i("retrofit" , "response: "+ response.body().toString());

                if(response.isSuccessful()){
                    adapter = new MyPagerAdapter(getSupportFragmentManager(),getLifecycle());
                    viewPager.setAdapter(adapter);
                    pb.setVisibility(View.INVISIBLE);

                    int cnt = response.body().getCnt();
                    if(cnt == 0) return;
                    for(int i=0; i<cnt ; i++) {
                         CityWeatherInfo cityWeatherInfo = response.body().getList().get(i);
                         Double temperature = cityWeatherInfo.getMain().getTemp();
                         String icon = cityWeatherInfo.getWeather().get(0).getIcon();
                         String description = cityWeatherInfo.getWeather().get(0).getDescription();
                         String cityname = cityWeatherInfo.getName();

                        Bundle args = new Bundle();
                        args.putString("city", cityname);
                        args.putDouble("temperature", temperature);
                        args.putString("iconid", icon);
                        args.putString("description", description);
                        //add new fragment to list
                        adapter.add(args);
                    }

                    updateDisplay();
                }
                else{
                    pb.setVisibility(View.INVISIBLE);
                    try {
                        Log.i("retrofit" , response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                pb.setVisibility(View.INVISIBLE);
                Log.i("retrofit" , t.getMessage());

            }
        });

    }

    private void updateDisplay() {
//        index=0;
//        transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container,weatherFragments.get(index));
//        transaction.commit();
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Locations").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(MainActivity.this, CitiesActivity.class));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    class MyPagerAdapter extends FragmentStateAdapter {

        private final List<Bundle> bundleList = new ArrayList<Bundle>();
        private FragmentManager fragmentManager;

        public MyPagerAdapter(@NonNull FragmentManager fm, Lifecycle lifecycle) {
            super(fm,lifecycle);
            this.fragmentManager = fm;
        }

        public void add(Bundle args){
            bundleList.add(args);
        }
        public void clear(){
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            for(WeatherCardFragment fragment : fragmentList){
//                transaction.detach(fragment);
//            }
//            transaction.commit();
            bundleList.clear();

        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return WeatherCardFragment.newInstance(bundleList.get(position));
        }

        @Override
        public int getItemCount() {
            return bundleList.size();
        }
    }


//    public void Next(View view) {
//        transaction = getSupportFragmentManager().beginTransaction();
//        index++;
//        if(index == weatherFragments.size()) index=0;
//        transaction.replace(R.id.fragment_container,weatherFragments.get(index));
//        transaction.commit();
//    }
}
