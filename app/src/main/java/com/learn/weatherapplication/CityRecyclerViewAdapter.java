package com.learn.weatherapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learn.weatherapplication.data.CityDbHelper;
import com.learn.weatherapplication.data.CityModel;

import java.util.List;

public class CityRecyclerViewAdapter extends RecyclerView.Adapter<CityRecyclerViewAdapter.ViewHolder> {

    List<CityModel> cityLists;
    CityDbHelper cityDbHelper;

    public CityRecyclerViewAdapter(List<CityModel> cityLists , CityDbHelper cityDbHelper){
        this.cityLists = cityLists;
        this.cityDbHelper = cityDbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityModel cityModel = cityLists.get(position);
        holder.city_name.setText(cityModel.toString());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityDbHelper.updateCitySelected(cityModel.getId(),false);
                cityLists.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityLists.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        Button btn;
        TextView city_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn);
            btn.setText("Delete");
            city_name  = itemView.findViewById(R.id.city_name_list);
        }
    }
}

//*********************************************************************************
