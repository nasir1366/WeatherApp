package com.learn.weatherapplication.data;


import java.util.ArrayList;
import java.util.List;


public class WeatherData {
    private Integer cnt;
    private List<CityWeatherInfo> list = new ArrayList<>();

    public List<CityWeatherInfo> getList() {
        return list;
    }

    public void setList(List<CityWeatherInfo> list) {
        this.list = list;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "cnt=" + cnt +
                ", list=" + list +
                '}';
    }
}


