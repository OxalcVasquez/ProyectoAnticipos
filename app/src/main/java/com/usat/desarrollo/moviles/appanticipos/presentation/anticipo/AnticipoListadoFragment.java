package com.usat.desarrollo.moviles.appanticipos.presentation.anticipo;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

import com.google.android.material.button.MaterialButton;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.AnticipoListadoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.AnticipoRegistroResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.HistorialAnticipoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.UltimaInstanciaResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.HistorialAnticipo;
import com.usat.desarrollo.moviles.appanticipos.presentation.adapter.AnticipoAdapter;
import com.usat.desarrollo.moviles.appanticipos.presentation.adapter.RendicionAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AnticipoListadoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView rvAnticipo;
    AnticipoAdapter adapter;
    ArrayList<Anticipo> listaAnticipo;
    ApiService apiService;
    SwipeRefreshLayout srlAnticipo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anticipo_listado, container, false);

        rvAnticipo=view.findViewById(R.id.rvAnticipo);
        rvAnticipo.setHasFixedSize(true);
        rvAnticipo.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        apiService = ApiAdapter.getApiService();
        srlAnticipo = view.findViewById(R.id.srlAnticipo);
        srlAnticipo.setOnRefreshListener(this);
        adapter = new AnticipoAdapter(this.getActivity());
        rvAnticipo.setAdapter(adapter);
        listar();
        return view;
    }

    public void listar(){
        apiService.getAnticipoListado(DatosSesion.sesion.getId(),DatosSesion.sesion.getToken()).enqueue(new Callback<AnticipoListadoResponse>() {
            @Override
            public void onResponse(Call<AnticipoListadoResponse> call, Response<AnticipoListadoResponse> response) {
                //Toast.makeText(getActivity(), ""+response.code(), Toast.LENGTH_SHORT).show();
                if (response.code() == 200){
                    AnticipoListadoResponse anticipo = response.body();
                    listaAnticipo = new ArrayList<>(Arrays.asList(anticipo.getData()));
                    adapter.cargarDatos(listaAnticipo);
                    srlAnticipo.setRefreshing(false);

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
                Log.e("Error listando anticipo", t.getMessage());
                Log.e("error listado",""+call);

            }
        });
    }

    public void actualizarEstado(String estadoAnticipo, String idAnticipo){
        Toast.makeText(getActivity(), ""+estadoAnticipo+" "+idAnticipo+" "+DatosSesion.sesion.getId(), Toast.LENGTH_SHORT).show();
        apiService.getAnticipoEvaluar(estadoAnticipo,idAnticipo,String.valueOf(DatosSesion.sesion.getId()),DatosSesion.sesion.getToken()).enqueue(new Callback<AnticipoRegistroResponse>() {
            @Override
            public void onResponse(Call<AnticipoRegistroResponse> call, Response<AnticipoRegistroResponse> response) {
                //Toast.makeText(getActivity(), ""+response.code(), Toast.LENGTH_SHORT).show();
                if (response.code() == 201){
                    if (response.body().getStatus()){ //True
                        Toast.makeText(getActivity(), "AAA", Toast.LENGTH_SHORT).show();
                    }
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
            public void onFailure(Call<AnticipoRegistroResponse> call, Throwable t) {
                Log.e("Error cambiando anticipo", t.getMessage());
            }
        });

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //Identificar la opción seleccionada en el menú contextual
        switch (item.getItemId()){
            case 1:
                //NO TOQUEN NADA DE ACAAAAAAAAAAAAAAAAAAA :C
                if(DatosSesion.sesion.getRol_id() == 1){
                    apiService.getUltimaInstancia(DatosSesion.sesion.getToken(), adapter.anticipoSeleccionado.getId(),"A",1).enqueue(new Callback<UltimaInstanciaResponse>() {
                        @Override
                        public void onResponse(Call<UltimaInstanciaResponse> call, Response<UltimaInstanciaResponse> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getContext(), "ID " + adapter.anticipoSeleccionado.getId(), Toast.LENGTH_SHORT).show();
                                UltimaInstanciaResponse ultimaInstanciaResponse = response.body();
                                boolean status = ultimaInstanciaResponse.getStatus();
                                if (status){
                                    HistorialAnticipo historialAnticipo =  ultimaInstanciaResponse.getData();
                                    final Dialog dialogHistorialAnticipo = new Dialog(getContext(), androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
                                    dialogHistorialAnticipo.setContentView(R.layout.dialog_historial_anticipo);
                                    dialogHistorialAnticipo.setCancelable(true);

                                    //Configure controls
                                    TextView txtInstanciaInforme = dialogHistorialAnticipo.findViewById(R.id.txt_instancia_anticipo);
                                    TextView txtEvaluador = dialogHistorialAnticipo.findViewById(R.id.txt_dialog_evaluador_anticipo);
                                    TextView txtEstado = dialogHistorialAnticipo.findViewById(R.id.txt_dialog_anticipo_estado);
                                    TextView txtDescripcion = dialogHistorialAnticipo.findViewById(R.id.txt_dialgo_descripcion_anticipo);
                                    MaterialButton btnCerrar = dialogHistorialAnticipo.findViewById(R.id.btn_cerrar_historial_anticipo);

//                                    HistorialAnticipo historialAnticipo = HistorialAnticipo.listaHistorial.get(HistorialAnticipo.listaHistorial.size()-1);
                                    txtDescripcion.setText(historialAnticipo.getDescripcion());
                                    txtEvaluador.setText(historialAnticipo.getEvaluador());

                                    if (historialAnticipo.getInstancia() != null){
                                        if (historialAnticipo.getInstancia().equalsIgnoreCase("Jefe de profesores")){
                                            txtInstanciaInforme.setText(getResources().getString(R.string.jefe_profesores));
                                        } else {
                                            txtInstanciaInforme.setText(getResources().getString(R.string.administrativo));
                                        }
                                    }

                                    switch (historialAnticipo.getEstado()) {
                                        case "REGISTRADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.register));
                                            txtEstado.setText(getResources().getString(R.string.estado_registrado));
                                            break;
                                        case "APROBADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.approved));
                                            txtEstado.setText(getResources().getString(R.string.estado_aprobado));
                                            break;
                                        case "DESIGNADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.approved));
                                            txtEstado.setText(getResources().getString(R.string.estado_designado));
                                            break;
                                        case "RECHAZADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.unapproved));
                                            txtEstado.setText(getResources().getString(R.string.estado_rechazado));
                                            break;
                                        case "RENDIDO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.register));
                                            txtEstado.setText(getResources().getString(R.string.estado_rendido));
                                            break;
                                        case "OBSERVADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.warning));
                                            txtEstado.setText(getResources().getString(R.string.estado_observado));
                                            break;
                                        case "PENDIENTE":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.secondaryDarkColor));
                                            txtEstado.setText(getResources().getString(R.string.estado_pendiente));
                                            break;
                                        case "RENDICION R":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.unapproved));
                                            txtEstado.setText(getResources().getString(R.string.estado_rendicionr));
                                            break;
                                        case "RENDICION A":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.approved));
                                            txtEstado.setText(getResources().getString(R.string.estado_rendiciona));
                                            break;
                                        case "CERRADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.primaryTextColor));
                                            txtEstado.setText(getResources().getString(R.string.estado_cerrado));
                                            break;
                                        case "SUBSANADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(),R.color.ic_launcher_background));
                                            txtEstado.setText(getResources().getString(R.string.estado_subsanado));
                                            break;
                                    }


                                    btnCerrar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogHistorialAnticipo.dismiss();
                                        }
                                    });



                                    dialogHistorialAnticipo.show();
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
                        public void onFailure(Call<UltimaInstanciaResponse> call, Throwable t) {
                            Log.e("Error", t.getMessage());
                        }
                    });

                } else {
                    String posicionItem = String.valueOf(AnticipoAdapter.listaAnticipo.get(adapter.posicionItemSeleccionadoRecyclerView).getId());
                    String estado = String.valueOf(3);
                    actualizarEstado(estado, posicionItem);
                }

                break;
            case 2:
                Toast.makeText(this.getActivity(), "Opcion Observar", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this.getActivity(), "Opcion Rechazar", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onRefresh() {
        listar();
    }
}