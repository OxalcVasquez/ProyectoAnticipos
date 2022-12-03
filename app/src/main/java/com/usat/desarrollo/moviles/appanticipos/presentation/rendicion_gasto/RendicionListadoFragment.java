package com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto;

import android.app.Dialog;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import com.google.android.material.snackbar.Snackbar;
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

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RendicionListadoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rvInformeGasto;
    RendicionAdapter rendicionAdapter;
    SwipeRefreshLayout srlRendicioListado;
    ArrayList<InformeGasto> listaInformesGasto;
    ApiService apiService;
    Boolean isDocente;


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
        isDocente = DatosSesion.sesion.getRol_id() == 1;

        srlRendicioListado = view.findViewById(R.id.srl_rendicion_listado);
        srlRendicioListado.setOnRefreshListener(this);
        srlRendicioListado.setColorSchemeResources(R.color.primaryColor,R.color.primaryDarkColor, R.color.primaryLightColor);


        rvInformeGasto = view.findViewById(R.id.recycler_rendicion_gastos);
        rvInformeGasto.setHasFixedSize(true);
        rvInformeGasto.setLayoutManager(new LinearLayoutManager(this.getContext()));
        if (!isDocente) {
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvInformeGasto);
        }

        rendicionAdapter = new RendicionAdapter(this.getContext());
        rvInformeGasto.setAdapter(rendicionAdapter);
        listarInformes();

        return view;
    }

    private void listarInformes() {
        if (isDocente) {
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
        } else {
            apiService.getInformeAdminListado(DatosSesion.sesion.getToken()).enqueue(new Callback<InformeGastoListadoResponse>() {
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
    }

    @Override
    public void onRefresh() {
        listarInformes();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (isDocente) {
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
        }
        return true;

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    rendicionAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"Se rechazó", Toast.LENGTH_SHORT).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    rendicionAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"Se aprobó", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.unapproved))
                    .addSwipeLeftActionIcon(R.drawable.ic_reject)
                    .addSwipeLeftLabel(getString(R.string.option_rechazar))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(getContext(), R.color.secondaryTextColor))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.approved))
                    .addSwipeRightActionIcon(R.drawable.ic_aprobar)
                    .addSwipeRightLabel(getString(R.string.option_aprobar))
                    .setSwipeRightLabelColor(ContextCompat.getColor(getContext(), R.color.secondaryTextColor))
                    .setActionIconTint(ContextCompat.getColor(getContext(), R.color.secondaryTextColor))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}