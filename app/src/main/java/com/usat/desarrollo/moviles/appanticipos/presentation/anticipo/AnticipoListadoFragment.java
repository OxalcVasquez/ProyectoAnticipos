package com.usat.desarrollo.moviles.appanticipos.presentation.anticipo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.AnticipoListadoResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.presentation.adapter.AnticipoAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AnticipoListadoFragment extends Fragment {
    RecyclerView rvAnticipo;
    AnticipoAdapter adapter;
    ArrayList<Anticipo> listaAnticipo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anticipo_listado, container, false);
        rvAnticipo=view.findViewById(R.id.rvAnticipo);
        rvAnticipo.setHasFixedSize(true);
        rvAnticipo.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        adapter = new AnticipoAdapter(this.getActivity());
        rvAnticipo.setAdapter(adapter);
        listar();
        return view;
    }

    public void listar(){
        ApiService apiService = ApiAdapter.getApiService();
        Call<AnticipoListadoResponse> call = apiService.getAnticipoListado(DatosSesion.TOKEN,"1");
        call.enqueue(new Callback<AnticipoListadoResponse>() {
            @Override
            public void onResponse(Call<AnticipoListadoResponse> call, Response<AnticipoListadoResponse> response) {
                if (response.code() == 200){
                    AnticipoListadoResponse anticipo = response.body();
                    listaAnticipo = new ArrayList<>(Arrays.asList(anticipo.getData()));
                    adapter.cargarDatos(listaAnticipo);
                }else{
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        Log.e("Error", jsonError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AnticipoListadoResponse> call, Throwable t) {

            }
        });

    }
}