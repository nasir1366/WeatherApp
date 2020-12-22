package com.learn.weatherapplication.data;

import java.util.ArrayList;
import java.util.List;

public class CityWeatherInfo {
    private Main main;
    private List<Weather> weather = new ArrayList<>();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "CityWeatherInfo{" +
                "main=" + main +
                ", weather=" + weather +
                ", name='" + name + '\'' +
                '}';
    }
}