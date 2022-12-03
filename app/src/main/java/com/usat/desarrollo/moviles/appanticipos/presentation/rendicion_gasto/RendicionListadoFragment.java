package com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.HistorialAnticipoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.InformeGastoListadoResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.HistorialAnticipo;
import com.usat.desarrollo.moviles.appanticipos.presentation.adapter.RendicionAdapter;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.InformeGasto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RendicionListadoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rvInformeGasto;
    RendicionAdapter rendicionAdapter;
    SwipeRefreshLayout srlRendicioListado;
    ArrayList<InformeGasto> listaInformesGasto;
    ApiService apiService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rendicion_listado, container, false);

        this.getActivity().setTitle(getString(R.string.title_rendicion_gastos));

        //Inicailzando api service
        apiService = ApiAdapter.getApiService();

        srlRendicioListado = view.findViewById(R.id.srl_rendicion_listado);
        srlRendicioListado.setOnRefreshListener(this);
        srlRendicioListado.setColorSchemeResources(R.color.primaryColor,R.color.primaryDarkColor, R.color.primaryLightColor);


        rvInformeGasto = view.findViewById(R.id.recycler_rendicion_gastos);
        rvInformeGasto.setHasFixedSize(true);
        rvInformeGasto.setLayoutManager(new LinearLayoutManager(this.getContext()));

        rendicionAdapter = new RendicionAdapter(this.getContext());
        rvInformeGasto.setAdapter(rendicionAdapter);
        listarInformes();

        return view;
    }

    private void listarInformes() {
        apiService.getInformeListado(DatosSesion.sesion.getToken(), DatosSesion.sesion.getId()).enqueue(new Callback<InformeGastoListadoResponse>() {
            @Override
            public void onResponse(Call<InformeGastoListadoResponse> call, Response<InformeGastoListadoResponse> response) {
                if (response.code() == 200){
                    InformeGastoListadoResponse informeGastoListadoResponse = response.body();
                    boolean status = informeGastoListadoResponse.getStatus();
                    if (status){
                        listaInformesGasto = (ArrayList<InformeGasto>) informeGastoListadoResponse.getData();
                        rendicionAdapter.cargarDatosRendicion(listaInformesGasto);
                        srlRendicioListado.setRefreshing(false);
                    } else {
                        try {
                            JSONObject jsonError = new JSONObject(response.errorBody().toString());
                            String message =  jsonError.getString("message");
                            Log.e("ERROR",message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<InformeGastoListadoResponse> call, Throwable t) {
                Log.e("Error listando informes", t.getMessage());
            }
        });

    }

    @Override
    public void onRefresh() {
        listarInformes();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1: //Menu eliminar
                apiService.getHistorial(DatosSesion.sesion.getToken(),RendicionAdapter.listadoInformes.get(rendicionAdapter.itemSeleccionado).getAnticipoId(),"I").enqueue(new Callback<HistorialAnticipoResponse>() {
                    @Override
                    public void onResponse(Call<HistorialAnticipoResponse> call, Response<HistorialAnticipoResponse> response) {
                        if (response.code() == 200) {
                            HistorialAnticipoResponse historialAnticipoResponse = response.body();
                            boolean status = historialAnticipoResponse.getStatus();
                            if (status){
                                HistorialAnticipo.listaHistorial = historialAnticipoResponse.getData();
                                final Dialog dialog = new Dialog(getContext(), androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
                                dialog.setContentView(R.layout.dialog_historial_informe);
                                dialog.setCancelable(true);

                                //Configure controls
                                TextView txtInstanciaInforme = dialog.findViewById(R.id.txt_instancia_informe);
                                TextView txtEvaluador = dialog.findViewById(R.id.txt_dialog_evaluador);
                                TextView txtEstado = dialog.findViewById(R.id.txt_dialog_informe_estado);
                                TextView txtDescripcion = dialog.findViewById(R.id.txt_dialgo_descripcion);

                                HistorialAnticipo historialAnticipo = HistorialAnticipo.listaHistorial.get(HistorialAnticipo.listaHistorial.size()-1);
                                txtInstanciaInforme.setText(historialAnticipo.getInstancia());
                                txtEvaluador.setText(historialAnticipo.getEvaluador());
                                txtEstado.setText(historialAnticipo.getEstado());
                                txtDescripcion.setText(historialAnticipo.getDescripcion());

                                dialog.show();
                            } else {
                                try {
                                    JSONObject jsonError = new JSONObject(response.errorBody().toString());
                                    String message =  jsonError.getString("message");
                                    Log.e("ERROR",message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<HistorialAnticipoResponse> call, Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

                break;
            case 2:
                break;
        }
        return true;

    }
}