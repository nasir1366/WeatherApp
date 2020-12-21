package com.learn.weatherapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class WeatherIcon extends androidx.appcompat.widget.AppCompatTextView {

    Map<String,String> iconMap = new HashMap<>();

    public WeatherIcon(@NonNull Context context) {
        super(context);
        init(context);
    }

    public WeatherIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeatherIcon(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        String[] key = getResources().getStringArray(R.array.my_icon_key);
        String[] values = getResources().getStringArray(R.array.my_icon_values);
        for(int i=0;i<key.length;i++){
            iconMap.put(key[i],values[i]);
        }

        Typeface weatherFont = Typeface.createFromAsset(context.getAssets(),"fonts/weatherfonts.ttf");
        setTypeface(weatherFont);
    }

    public void setWeatherIcon(String id){
        String icon_code ;
        if(iconMap.containsKey(id))
            icon_code = iconMap.get(id);
        else
            icon_code="&#xf00d;";//wi_day_sunny


        if(id.endsWith("n"))
            setTextColor(Color.BLACK);
        else
            setTextColor(Color.parseColor("#ffff8800"));

        setText(Html.fromHtml(icon_code));
    }


}
