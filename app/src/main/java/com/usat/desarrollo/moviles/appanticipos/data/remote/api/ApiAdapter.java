package com.usat.desarrollo.moviles.appanticipos.data.remote.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiAdapter {
    private static ApiService apiService;

//    public static final String BASE_URL = "https://web-services-anticipos.onrender.com/";
    public static final String BASE_URL = "http://192.168.1.2:3001/";
    //public static final String BASE_URL = "http://192.168.0.6:3001";



    public static ApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
