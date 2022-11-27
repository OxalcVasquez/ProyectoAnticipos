package com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.AnticipoListadoResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.presentation.adapter.ComprobanteAdapter;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Comprobante;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RendicionGastosFragment extends Fragment {

    TextView txtDocente, txtPasajes, txtPasajesDe, txtAlimentacion, txtAlimentacionDe, txtHotel, txtHotelDe, txtMovilidad, txtMovilidadDe, txtDevolucion, txtRestante;
    AutoCompleteTextView actvAnticipos;
    Button btnAgregarComprobante, btnRegistrarRedencion;
    int pos = 0;

    RecyclerView recyclerComprobante;
    ComprobanteAdapter comprobanteAdapter;

    ApiService apiService;

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
        btnAgregarComprobante = view.findViewById(R.id.btn_comprobante_agregar_rendicion);
        btnRegistrarRedencion = view.findViewById(R.id.btn_registrar_rendicion);

        txtDocente.setText(DatosSesion.sesion.getNombres() + " "+DatosSesion.sesion.getApellidos());



        recyclerComprobante = view.findViewById(R.id.recycler_comprobantes_rendicion);
        recyclerComprobante.setHasFixedSize(true);
        recyclerComprobante.setLayoutManager(new LinearLayoutManager(this.getContext()));

        comprobanteAdapter = new ComprobanteAdapter(this.getContext());
        recyclerComprobante.setAdapter(comprobanteAdapter);
        listar();

        getPos();

        if (recyclerComprobante.getAdapter().getItemCount() == 0) {
            recyclerComprobante.setVisibility(View.GONE);
        }

        cargarAnticiposPendientesARendicion();
        return view;
    }

    private void registrarRendicion() {
        btnRegistrarRedencion.setOnClickListener(view -> {
            if (validate()) {
                Toast.makeText(this.getActivity(), R.string.dialog_agregar_comprobante, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarComprobante() {
        btnAgregarComprobante.setOnClickListener(view -> {
        });
    }

    private void getPos() {
        actvAnticipos.setOnItemClickListener((adapterView, view, i, l) -> {
            pos = i;
        });
    }

    private boolean validate() {
        return actvAnticipos.getText().toString().equalsIgnoreCase("");
    }

    private void listar() {
        new Comprobante().cargarDatosComprobante();
        comprobanteAdapter.cargarDatosComprobante(Comprobante.comprobanteListado);
    }

    private void cargarAnticiposPendientesARendicion(){
        apiService.getAnticiposEstados(DatosSesion.sesion.getId(),7,DatosSesion.sesion.getToken()).enqueue(new Callback<AnticipoListadoResponse>() {
            @Override
            public void onResponse(Call<AnticipoListadoResponse> call, Response<AnticipoListadoResponse> response) {
                if (response.code() == 200){
                    AnticipoListadoResponse anticipoListadoResponse = response.body();
                    if (anticipoListadoResponse.getStatus()){
                        Anticipo.listaAnticipos = new ArrayList<>(Arrays.asList(anticipoListadoResponse.getData()));
                        String descripcion[] = new String[anticipoListadoResponse.getData().length];
                        for (int i = 0; i <anticipoListadoResponse.getData().length ; i++) {
                            descripcion[i] = anticipoListadoResponse.getData()[i].getDescripcion();
                        }
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
}