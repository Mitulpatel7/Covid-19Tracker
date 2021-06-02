package com.mitulpatel.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mitulpatel.covid_19tracker.api.ApiUtilities;
import com.mitulpatel.covid_19tracker.api.CountryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryActivity extends AppCompatActivity implements custom_adapter_country_data.onCountryClickListener{

    RecyclerView recyclerView;
    List<CountryData> countryDataList;
    ProgressDialog dialog;
    EditText search_view;
    custom_adapter_country_data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        search_view = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recyclerview);
        countryDataList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        data = new custom_adapter_country_data(this, countryDataList,this);
        recyclerView.setAdapter(data);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.show();

        ApiUtilities.apiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                countryDataList.addAll(response.body());
                data.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CountryActivity.this, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        search_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });



    }


    @Override
    public void onCountryClick(int position) {
        String country = countryDataList.get(position).getCountry().toString();
        Log.d( "onCountryClick: ",country.toString());
        Intent intent = new Intent(CountryActivity.this,MainActivity.class);
        intent.putExtra("name",country);
        startActivity(intent);

    }


    private void filter(String toString) {

        List<CountryData> filterlistitem = new ArrayList<>();
        for(CountryData items : countryDataList){
            if (items.getCountry().toLowerCase().contains(toString.toLowerCase())){
                filterlistitem.add(items);
            }
        }
        data.filterlist(filterlistitem);


    }
}