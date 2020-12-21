package com.learn.weatherapplication;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.learn.weatherapplication.data.CityDbHelper;
import com.learn.weatherapplication.data.CityModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.List;

public class CitiesActivity extends AppCompatActivity implements AddCityFragment.AddCityInterface{
    RecyclerView recyclerView;
    CityRecyclerViewAdapter adapter;
    CityDbHelper dbHelper;
    List<CityModel> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .add(new AddCityFragment(),null)
                        .commit();
            }
        });

        dbHelper = new CityDbHelper(this);
        recyclerView = findViewById(R.id.city_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        updateDispaly();
    }

    private void updateDispaly(){
        cityList = dbHelper.getCities("selected = 1",null, null);
        adapter = new CityRecyclerViewAdapter(cityList,dbHelper);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void addCity(Long id) {
        dbHelper.updateCitySelected(id,true);
        updateDispaly();
    }
}