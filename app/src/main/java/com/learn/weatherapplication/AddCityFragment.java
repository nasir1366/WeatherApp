package com.learn.weatherapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learn.weatherapplication.data.CityDbHelper;
import com.learn.weatherapplication.data.CityModel;

import java.util.List;

public class AddCityFragment extends DialogFragment {

    private RecyclerView recyclerView;
    private androidx.appcompat.widget.SearchView searchView;
    List<CityModel> cityModelList ;
    CityDbHelper dbHelper;
    SelectCityRecyclerViewAdapter adapter;
    AddCityInterface iActivity;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_city, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_selectcity);
        searchView = view.findViewById(R.id.search_view_selectcity);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new CityDbHelper(getContext());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cityModelList = dbHelper.getCities("name LIKE '%" + query + "%'",null, "20");
                updateDisplay();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }

    private void updateDisplay() {
        adapter = new SelectCityRecyclerViewAdapter(cityModelList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(adapter);

    }

//****************************************************************************************************

    private class SelectCityRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<CityModel> cityList;

        public SelectCityRecyclerViewAdapter(List<CityModel> cityList) {
            this.cityList = cityList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item,parent,false);
            return new MyViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            CityModel cityModel = cityList.get(position);
            holder.city_name.setText(cityModel.toString());
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iActivity.addCity(cityModel.getId());
                    dismiss();
                }
            });

        }

        @Override
        public int getItemCount() {
            return cityList.size();
        }
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        Button btn;
        TextView city_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn);
            btn.setText("Add");
            city_name  = itemView.findViewById(R.id.city_name_list);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iActivity=(AddCityInterface) context;
    }

    static interface AddCityInterface{
        void addCity(Long id);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeDialogSize();
    }

    private void changeDialogSize(){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getDialog().getWindow().setLayout(
                (int) (metrics.widthPixels * 0.9),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

    }
}