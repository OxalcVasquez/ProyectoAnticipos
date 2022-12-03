package com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.AnticipoListadoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.InformeGastoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.TarifaResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Tarifa;
import com.usat.desarrollo.moviles.appanticipos.presentation.adapter.ComprobanteAdapter;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Comprobante;
import com.usat.desarrollo.moviles.appanticipos.presentation.comprobante.AgregarComprobanteActivity;
import com.usat.desarrollo.moviles.appanticipos.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RendicionGastosFragment extends Fragment implements View.OnClickListener {

    TextView txtDocente, txtPasajes, txtPasajesDe, txtAlimentacion, txtAlimentacionDe, txtHotel, txtHotelDe, txtMovilidad, txtMovilidadDe, txtDevolucion, txtRestante, txtMontoRendir;
    AutoCompleteTextView actvAnticipos;
    Button btnAgregarComprobante, btnRegistrarRedencion;
    ScrollView svComprobantes;
    RecyclerView recyclerComprobante;
    ComprobanteAdapter comprobanteAdapter;
    float restante;
    public static final int REQUEST_COMPROBANTES = 2;

    Anticipo anticipoSeleccionado = new Anticipo();
    ApiService apiService;
    double montoPasajes = 0,montoAlimentacion = 0,montoHotel = 0,montoMovilidad = 0, montoDevolucion = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rendicion_gastos, container, false);

        this.getActivity().setTitle(getString(R.string.title_registro_rendicion_gastos));

        txtDocente = view.findViewById(R.id.txt_docente_rendicion);
        actvAnticipos = view.findViewById(R.id.actv_anticipos_rendicion);
        txtPasajes = view.findViewById(R.id.txt_pasajes_rendicion);
        txtPasajesDe = view.findViewById(R.id.txt_pasajes_de_rendicion);
        txtAlimentacion = view.findViewById(R.id.txt_alimentacion_rendicion);
        txtAlimentacionDe = view.findViewById(R.id.txt_alimentacion_de_rendicion);
        txtHotel = view.findViewById(R.id.txt_hotel_rendicion);
        txtHotelDe = view.findViewById(R.id.txt_hotel_de_rendicion);
        txtMovilidad = view.findViewById(R.id.txt_movilidad_rendicion);
        txtMovilidadDe = view.findViewById(R.id.txt_movilidad_de_rendicion);
        txtDevolucion = view.findViewById(R.id.txt_devolucion_rendicion);
        txtRestante = view.findViewById(R.id.txt_restante_rendicion);
        txtMontoRendir = view.findViewById(R.id.txt_monto_rendir);
        btnAgregarComprobante = view.findViewById(R.id.btn_comprobante_agregar_rendicion);
        btnRegistrarRedencion = view.findViewById(R.id.btn_registrar_rendicion);
        svComprobantes = view.findViewById(R.id.sv_comprobantes);

        txtDocente.setText(DatosSesion.sesion.getNombres() + " "+DatosSesion.sesion.getApellidos());
        txtDocente.setTextColor(ContextCompat.getColor(getActivity(),R.color.primaryColor));

        //Inicailzando api service
        apiService = ApiAdapter.getApiService();

        recyclerComprobante = view.findViewById(R.id.recycler_comprobantes_rendicion);
        recyclerComprobante.setHasFixedSize(true);
        recyclerComprobante.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        comprobanteAdapter = new ComprobanteAdapter(this.getActivity());
//        recyclerComprobante.setAdapter(comprobanteAdapter);

//        listar();

        //Para obtener id cada vez que sse cambie
        actvAnticipos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                anticipoSeleccionado = Anticipo.listaAnticipos.get(i);
                cargarComprobantes();
            }
        });
        cargarAnticiposPendientesARendicion();
        btnRegistrarRedencion.setOnClickListener(this);
        btnAgregarComprobante.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_comprobante_agregar_rendicion:
                agregarComprobante();
                break;
            case R.id.btn_registrar_rendicion:
                Helper.mensajeConfirmacion(RendicionGastosFragment.this.getActivity(),"CONFIRMACION","Desea grabar informe?","SI","NO",new TaskGrabarRendicion());
                break;
        }
    }


    private void registrarRendicion() {
                int anticipoId = anticipoSeleccionado.getId();
                JSONArray jsonArray = new JSONArray();
                for (Comprobante comprobante : ComprobanteAdapter.comprobanteList){
                    jsonArray.put(comprobante.getJSONComprobantes());
                }
                String detalleComprobantes = jsonArray.toString();
                Log.e("ERROR", detalleComprobantes);
                if (!validarComprobantes()){
                    if (restante == 0){
                        apiService.getInformeRegistrado(DatosSesion.sesion.getToken(), anticipoId,detalleComprobantes).enqueue(new Callback<InformeGastoResponse>() {
                            @Override
                            public void onResponse(Call<InformeGastoResponse> call, Response<InformeGastoResponse> response) {
                                if (response.code() == 201){
                                    if (response.body().getStatus()){
                                        String numInforme = response.body().getData().getNumInforme();
                                        String informeGrabado = getResources().getString(R.string.informe_info) + "\n\n" +
                                                getResources().getString(R.string.informe_redencion) + " : " + numInforme + "\n"+
                                                getResources().getString(R.string.devolucion_rendicion_registro) + txtDevolucion.getText() + "\n";
                                        Helper.mensajeInformacion(getActivity(),getResources().getString(R.string.informe_redencion),informeGrabado);
                                    }

                                } else if (response.code() == 500) {
                                    try {
                                        JSONObject error = new JSONObject(response.errorBody().string());
                                        Helper.mensajeInformacion(getActivity(),"ERROR",error.getString("message"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    Log.e("ERROR ---->",response.message());
                                    Toast.makeText(getActivity(), "ERROR : "+response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<InformeGastoResponse> call, Throwable t) {
                                Log.e("Error registrando gastos", t.getMessage());

                            }
                        });

                    } else {
                        Helper.mensajeInformacion(getActivity(),"INFO",getResources().getString(R.string.txt_validation_informe));
                    }

                } else {
                    Toast.makeText(this.getActivity(), R.string.dialog_agregar_comprobante, Toast.LENGTH_SHORT).show();
                }

    }

    private void agregarComprobante() {
        if (anticipoSeleccionado.getId()!=0) {
                startActivityForResult(new Intent(this.getActivity(),AgregarComprobanteActivity.class), REQUEST_COMPROBANTES);// Activity is started with requestCode 2
        } else {
            Helper.mensajeInformacion(getActivity(),"Informacion","Debes seleccionar un anticipo");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_COMPROBANTES && resultCode == Activity.RESULT_OK) {
            cargarComprobantes();
        }
    }

    private boolean validarComprobantes() {
        return ComprobanteAdapter.comprobanteList.isEmpty();
    }

    private void  cargarComprobantes() {

        if (Comprobante.comprobanteListado.size()>0){
            recyclerComprobante.setVisibility(View.VISIBLE);
            svComprobantes.setVisibility(View.VISIBLE);
        }
        comprobanteAdapter = new ComprobanteAdapter(getActivity());
        recyclerComprobante.setAdapter(comprobanteAdapter);
        comprobanteAdapter.cargarDatosComprobante(Comprobante.comprobanteListado);

        calcularMontosPorRendir();
        double montosRendidos[] = comprobanteAdapter.calcularTotales();
        txtPasajes.setText("S./ " + montosRendidos[0]);
        txtAlimentacion.setText("S./ " + montosRendidos[1]);
        txtHotel.setText("S./ " + montosRendidos[2]);
        txtMovilidad.setText("S./ " + montosRendidos[3]);
        txtDevolucion.setText("S./ " + montosRendidos[4]);
        restante = anticipoSeleccionado.getMonto_total();

        if (montosRendidos[0] > montoPasajes) {
            restante -= montoPasajes;
        } else {
            restante -= montosRendidos[0];
        }

        if (montosRendidos[1] > montoAlimentacion) {
            restante -= montoAlimentacion;
        } else {
            restante -= montosRendidos[1];
        }

        if (montosRendidos[2] > montoHotel) {
            restante -= montoHotel;
        } else {
            restante -= montosRendidos[2];
        }

        if (montosRendidos[3] > montoMovilidad) {
            restante -= montoMovilidad;
        } else {
            restante -= montosRendidos[3];
        }
        if (montosRendidos[4] > restante){
            restante = 0;
        } else {
            restante -= montosRendidos[4];

        }

        txtRestante.setText("S./" + restante);
    }

    private void calcularMontosPorRendir() {
        Toast.makeText(getActivity(), "holaaa " + anticipoSeleccionado.getSede_id(), Toast.LENGTH_SHORT).show();

        txtMontoRendir.setText("S./" + anticipoSeleccionado.getMonto_total());
        txtRestante.setText("S./" + anticipoSeleccionado.getMonto_total());

        int dias = Helper.diasEntreDosFechas(Helper.formatearAMD_to_DMA(anticipoSeleccionado.getFecha_inicio()),Helper.formatearAMD_to_DMA(anticipoSeleccionado.getFecha_fin()));
        apiService.getViaticos(DatosSesion.sesion.getToken(),anticipoSeleccionado.getSede_id(),dias).enqueue(new Callback<TarifaResponse>() {
            @Override
            public void onResponse(Call<TarifaResponse> call, Response<TarifaResponse> response) {
                if (response.code() == 200) {
                    TarifaResponse tarifaResponse = response.body();
                    boolean status = tarifaResponse.getStatus();
                    if (status) {
                        List<Tarifa> tarifaList = tarifaResponse.getData();
                        for (Tarifa tarifa:tarifaList ) {
                            switch (tarifa.getRubro_id()){
                                case 1 :
                                    montoPasajes = tarifa.getMonto_maximo();
                                    txtPasajesDe.setText("S./ " + montoPasajes);
                                    break;
                                case 2:
                                    montoAlimentacion = tarifa.getMonto_maximo();
                                    txtAlimentacionDe.setText("S./ " + montoAlimentacion);
                                    break;
                                case 3 :
                                    montoHotel = tarifa.getMonto_maximo();
                                    txtHotelDe.setText("S./ " + montoHotel);
                                    break;
                                case 4:
                                    montoMovilidad = tarifa.getMonto_maximo();
                                    txtMovilidadDe.setText("S./ " + montoMovilidad);
                                    break;
                            }

                        }
                    } else {
                        try {
                            JSONObject jsonError = new JSONObject(response.errorBody().string());
                            String message =  jsonError.getString("message");
                            Log.e("ERROR CARGANDO RESUMEN VIATICOS", message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TarifaResponse> call, Throwable t) {

            }
        });
    }


    private void cargarAnticiposPendientesARendicion(){
        apiService.getAnticipoListado(DatosSesion.sesion.getId(),DatosSesion.sesion.getToken()).enqueue(new Callback<AnticipoListadoResponse>() {
            @Override
            public void onResponse(Call<AnticipoListadoResponse> call, Response<AnticipoListadoResponse> response) {
                if (response.code() == 200){
                    AnticipoListadoResponse anticipoListadoResponse = response.body();
                    if (anticipoListadoResponse.getStatus()){
                        Anticipo.listaAnticipos = new ArrayList<>(Arrays.asList(anticipoListadoResponse.getData()));
                        List<String> listaAnticipo = new ArrayList<String>();
                        for (int i = 0; i <anticipoListadoResponse.getData().length ; i++) {
                            if (anticipoListadoResponse.getData()[i].getEstado().equalsIgnoreCase("RENDICION R") || anticipoListadoResponse.getData()[i].getEstado().equalsIgnoreCase("PENDIENTE")){
                                listaAnticipo.add(anticipoListadoResponse.getData()[i].getDescripcion());
                            }
                        }

                        String descripcion[] = listaAnticipo.toArray(new String[0]);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,descripcion);
                        actvAnticipos.setAdapter(adapter);
                    }

                }   else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        String message =  jsonError.getString("message");
                        Log.e("ERROR CARGANDO LISTADO MOTIVOS", message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<AnticipoListadoResponse> call, Throwable t) {
                Log.e("Error cargando  anticipos", t.getMessage());

            }
        });
    }
    class TaskGrabarRendicion implements Runnable{
        @Override
        public void run() {
            registrarRendicion();
        }
    }

}