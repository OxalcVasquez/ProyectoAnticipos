package com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.ComprobanteInformeListadoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.HistorialAnticipoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.InformeGastoListadoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.UltimaInstanciaResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.ValidacionResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Comprobante;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.HistorialAnticipo;
import com.usat.desarrollo.moviles.appanticipos.presentation.adapter.AnticipoAdapter;
import com.usat.desarrollo.moviles.appanticipos.presentation.adapter.ComprobanteAdapter;
import com.usat.desarrollo.moviles.appanticipos.presentation.adapter.RendicionAdapter;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.InformeGasto;
import com.usat.desarrollo.moviles.appanticipos.presentation.comprobante.AgregarComprobanteActivity;

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
    RecyclerView recyclerComprobante;
    ComprobanteAdapter comprobanteAdapter;
    ProgressBar progressBar;
    ScrollView sv_comprobantes_informe;

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
        srlRendicioListado.setColorSchemeResources(R.color.primaryColor, R.color.primaryDarkColor, R.color.primaryLightColor);


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
                    if (response.code() == 200) {
                        InformeGastoListadoResponse informeGastoListadoResponse = response.body();
                        boolean status = informeGastoListadoResponse.getStatus();
                        if (status) {
                            listaInformesGasto = (ArrayList<InformeGasto>) informeGastoListadoResponse.getData();
                            rendicionAdapter.cargarDatosRendicion(listaInformesGasto);
                            srlRendicioListado.setRefreshing(false);
                        } else {
                            try {
                                JSONObject jsonError = new JSONObject(response.errorBody().toString());
                                String message = jsonError.getString("message");
                                Log.e("ERROR", message);
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
                    if (response.code() == 200) {
                        InformeGastoListadoResponse informeGastoListadoResponse = response.body();
                        boolean status = informeGastoListadoResponse.getStatus();
                        if (status) {
                            listaInformesGasto = (ArrayList<InformeGasto>) informeGastoListadoResponse.getData();
                            rendicionAdapter.cargarDatosRendicion(listaInformesGasto);
                            srlRendicioListado.setRefreshing(false);
                        } else {
                            try {
                                JSONObject jsonError = new JSONObject(response.errorBody().toString());
                                String message = jsonError.getString("message");
                                Log.e("ERROR", message);
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
            switch (item.getItemId()) {
                case 1: //Menu eliminar
                    apiService.getUltimaInstancia(DatosSesion.sesion.getToken(), rendicionAdapter.informeGastoSeleccionado.getAnticipoId(), "I", 1).enqueue(new Callback<UltimaInstanciaResponse>() {
                        @Override
                        public void onResponse(Call<UltimaInstanciaResponse> call, Response<UltimaInstanciaResponse> response) {
                            if (response.code() == 200) {
                                UltimaInstanciaResponse ultimaInstanciaResponse = response.body();
                                boolean status = ultimaInstanciaResponse.getStatus();
                                if (status) {
                                    Toast.makeText(getContext(), "ID " + RendicionAdapter.listadoInformes.get(rendicionAdapter.itemSeleccionado).getAnticipoId(), Toast.LENGTH_SHORT).show();
                                    HistorialAnticipo historialAnticipo = ultimaInstanciaResponse.getData();
                                    final Dialog dialogHistorialInforme = new Dialog(getContext(), androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
                                    dialogHistorialInforme.setContentView(R.layout.dialog_historial_informe);
                                    dialogHistorialInforme.setCancelable(true);

                                    //Configure controls
                                    TextView txtInstanciaInforme = dialogHistorialInforme.findViewById(R.id.txt_instancia_informe);
                                    TextView txtEvaluador = dialogHistorialInforme.findViewById(R.id.txt_dialog_evaluador);
                                    TextView txtEstado = dialogHistorialInforme.findViewById(R.id.txt_dialog_informe_estado);
                                    TextView txtDescripcion = dialogHistorialInforme.findViewById(R.id.txt_dialgo_descripcion);
                                    MaterialButton btnCerrar = dialogHistorialInforme.findViewById(R.id.btn_cerrar_historial_informe);

                                    txtEvaluador.setText(historialAnticipo.getEvaluador());
                                    txtDescripcion.setText(historialAnticipo.getDescripcion());

                                    if (historialAnticipo.getInstancia() != null) {
                                        if (historialAnticipo.getInstancia().equalsIgnoreCase("Jefe de profesores")) {
                                            txtInstanciaInforme.setText(getResources().getString(R.string.jefe_profesores));
                                        } else {
                                            txtInstanciaInforme.setText(getResources().getString(R.string.administrativo));
                                        }
                                    }


                                    switch (historialAnticipo.getEstado()) {
                                        case "REGISTRADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.register));
                                            txtEstado.setText(getResources().getString(R.string.estado_registrado));
                                            break;
                                        case "APROBADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.approved));
                                            txtEstado.setText(getResources().getString(R.string.estado_aprobado));
                                            break;
                                        case "DESIGNADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.approved));
                                            txtEstado.setText(getResources().getString(R.string.estado_designado));
                                            break;
                                        case "RECHAZADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.unapproved));
                                            txtEstado.setText(getResources().getString(R.string.estado_rechazado));
                                            break;
                                        case "RENDIDO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.register));
                                            txtEstado.setText(getResources().getString(R.string.estado_rendido));
                                            break;
                                        case "OBSERVADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.warning));
                                            txtEstado.setText(getResources().getString(R.string.estado_observado));
                                            break;
                                        case "PENDIENTE":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.secondaryDarkColor));
                                            txtEstado.setText(getResources().getString(R.string.estado_pendiente));
                                            break;
                                        case "RENDICION R":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.unapproved));
                                            txtEstado.setText(getResources().getString(R.string.estado_rendicionr));
                                            break;
                                        case "RENDICION A":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.approved));
                                            txtEstado.setText(getResources().getString(R.string.estado_rendiciona));
                                            break;
                                        case "CERRADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryTextColor));
                                            txtEstado.setText(getResources().getString(R.string.estado_cerrado));
                                            break;
                                        case "SUBSANADO":
                                            txtEstado.setTextColor(ContextCompat.getColor(getContext(), R.color.ic_launcher_background));
                                            txtEstado.setText(getResources().getString(R.string.estado_subsanado));
                                            break;
                                    }

                                    btnCerrar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogHistorialInforme.dismiss();
                                        }
                                    });


                                    dialogHistorialInforme.show();
                                } else {
                                    try {
                                        JSONObject jsonError = new JSONObject(response.errorBody().toString());
                                        String message = jsonError.getString("message");
                                        Log.e("ERROR", message);
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

                    break;
                case 2:
                    Fragment fragment = new RendicionFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("anticipoId",RendicionAdapter.listadoInformes.get(rendicionAdapter.itemSeleccionado).getAnticipoId());
//                    fragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_redencion, fragment);
                    fragmentTransaction.commit();
                    break;
            }
        } else if (item.getItemId() == 3) {
            final Dialog dialogComprobanteInforme = new Dialog(getContext(), androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
            dialogComprobanteInforme.setContentView(R.layout.dialog_comprobante_informe);
            dialogComprobanteInforme.setCancelable(true);

            progressBar = dialogComprobanteInforme.findViewById(R.id.progressBarComprobantes);
            sv_comprobantes_informe = dialogComprobanteInforme.findViewById(R.id.sv_comprobantes_informe);
            recyclerComprobante = dialogComprobanteInforme.findViewById(R.id.recycler_comprobantes_informe);
            recyclerComprobante.setHasFixedSize(true);
            recyclerComprobante.setLayoutManager(new LinearLayoutManager(dialogComprobanteInforme.getContext()));
            MaterialButton btnCerrar = dialogComprobanteInforme.findViewById(R.id.btn_cerrar_comprobante_informe);

            comprobanteAdapter = new ComprobanteAdapter(dialogComprobanteInforme.getContext());
            recyclerComprobante.setAdapter(comprobanteAdapter);

            btnCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogComprobanteInforme.dismiss();
                }
            });

            dialogComprobanteInforme.show();

            apiService.getComprobantesInformeListado(
                    DatosSesion.sesion.getToken(),
                    rendicionAdapter.informeGastoSeleccionado.getId()
            ).enqueue(new Callback<ComprobanteInformeListadoResponse>() {
                @Override
                public void onResponse(Call<ComprobanteInformeListadoResponse> call, Response<ComprobanteInformeListadoResponse> response) {
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        sv_comprobantes_informe.setVisibility(View.VISIBLE);
                        ComprobanteInformeListadoResponse comprobanteInformeListadoResponse = response.body();
                        boolean status = comprobanteInformeListadoResponse.getStatus();
                        if (status) {
                            Comprobante.comprobanteListado.clear();
                            Comprobante.comprobanteListado.addAll(comprobanteInformeListadoResponse.getData());

                            comprobanteAdapter.cargarDatosComprobante(Comprobante.comprobanteListado);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            try {
                                JSONObject jsonError = new JSONObject(response.errorBody().toString());
                                String message = jsonError.getString("message");
                                Log.e("ERROR", message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ComprobanteInformeListadoResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("prueba", t.getMessage());
                }
            });
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
                    Toast.makeText(getContext(), "Se rechazó", Toast.LENGTH_SHORT).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    rendicionAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Se aprobó", Toast.LENGTH_SHORT).show();
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