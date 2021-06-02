package com.mitulpatel.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mitulpatel.covid_19tracker.api.ApiUtilities;
import com.mitulpatel.covid_19tracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView confirm , active , recovered , deaths , today_confirm , today_recovered , today_death , test , last__update , country_name;

    List<CountryData> countryDataList;

    PieChart pieChart;

    String country = "India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        pieChart = findViewById(R.id.piechart);
        last__update = findViewById(R.id.update_time);
        confirm = findViewById(R.id.today_confirm);
        active = findViewById(R.id.today_active);
        recovered = findViewById(R.id.today_recoverd);
        deaths = findViewById(R.id.today_death);
        test = findViewById(R.id.today_tests);
        today_death = findViewById(R.id.today_increase_death);
        today_confirm = findViewById(R.id.today_increase_confrim);
        today_recovered = findViewById(R.id.today_recover_increase);
        country_name = findViewById(R.id.country_name_main);

        countryDataList = new ArrayList<>();
        country_name.setText(country);
        if(getIntent().getStringExtra("name") != null){
            country = getIntent().getStringExtra("name");
        }

        ApiUtilities.apiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                    countryDataList.addAll(response.body());

                    for(int i=0;i<countryDataList.size();i++){
                        if ( countryDataList.get(i).getCountry().equals(country)){

                            country_name.setText(country);
                            int confirmcase = Integer.parseInt(countryDataList.get(i).getCases());
                            int activecase = Integer.parseInt(countryDataList.get(i).getActive());
                            int recoveredcase = Integer.parseInt(countryDataList.get(i).getRecovered());
                            int deathscase = Integer.parseInt(countryDataList.get(i).getDeaths());
                            int today_confirmcase = Integer.parseInt(countryDataList.get(i).getTodayCases());
                            int today_recoveredcase = Integer.parseInt(countryDataList.get(i).getTodayRecovered());
                            int today_deathscase = Integer.parseInt(countryDataList.get(i).getTodayDeaths());
                            int testscase = Integer.parseInt(countryDataList.get(i).getTests());


                            setText(countryDataList.get(i).getUpdated());

                            confirm.setText(NumberFormat.getInstance().format(confirmcase));
                            active.setText(NumberFormat.getInstance().format(activecase));
                            recovered.setText(NumberFormat.getInstance().format(recoveredcase));
                            deaths.setText(NumberFormat.getInstance().format(deathscase));
                            test.setText(NumberFormat.getInstance().format(testscase));
                            today_confirm.setText("[+"+NumberFormat.getInstance().format(today_confirmcase)+"]");
                            today_recovered.setText("[+"+NumberFormat.getInstance().format(today_recoveredcase)+"]");
                            today_death.setText("[+"+NumberFormat.getInstance().format(today_deathscase)+"]");

                            pieChart.addPieSlice(new PieModel("Confirm",confirmcase,getResources().getColor(R.color.yellow)));
                            pieChart.addPieSlice(new PieModel("Active",activecase,getResources().getColor(R.color.light_Blue)));
                            pieChart.addPieSlice(new PieModel("Recovered",recoveredcase,getResources().getColor(R.color.green)));
                            pieChart.addPieSlice(new PieModel("Deaths",deathscase,getResources().getColor(R.color.red)));

                            pieChart.startAnimation();

                        }
                    }


            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setText(String updated) {

        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

        long miliseconds = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(miliseconds);

        last__update.setText("Updated at "+dateFormat.format(calendar.getTime()));

    }

    public void CountryData(View view) {

        Intent intent = new Intent(MainActivity.this,CountryActivity.class);
        startActivity(intent);
    }
}