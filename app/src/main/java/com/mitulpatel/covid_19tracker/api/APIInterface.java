package com.mitulpatel.covid_19tracker.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    static final String BASE_URL = "https://corona.lmao.ninja/v2/";

    @GET("countries")
    public Call<List<CountryData>> getCountryData();
}
