package com.usat.desarrollo.moviles.appanticipos.presentation.anticipo;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.AnticipoRegistroResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.MotivoAnticipoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.SedesResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.TarifaResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.ValidacionResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.MotivoAnticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Sede;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Tarifa;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Validacion;
import com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto.RendicionGastosFragment;
import com.usat.desarrollo.moviles.appanticipos.utils.Helper;
import com.usat.desarrollo.moviles.appanticipos.utils.Pickers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SolicitudAnticipoFragment extends Fragment implements View.OnClickListener {

    TextView txtDescripcion,txtTotalViaticos,txtPasajes, txtAlimentacion, txtHotel, txtMovilidad;
    EditText txtFechaInicio,txtFechaFin;
    MaterialButton btnRegistrarAnticipo,btnLimpiar;
    AutoCompleteTextView actvMotivoAnticipo, actvSedeDestino;
    ProgressBar progressBar;
    int idMotivoSeleccionado,idSedeSeleccionada;
    int diasAnticipo=1;
    ApiService apiService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitud_anticipo, container, false);
        getActivity().setTitle("Solicitar Anticipo");
        txtDescripcion = view.findViewById(R.id.txtDescripcion);
        txtFechaInicio = view.findViewById(R.id.txtFechaInicio);
        txtFechaFin = view.findViewById(R.id.txtFechaFin);
        txtTotalViaticos = view.findViewById(R.id.txt_total_viaticos);
        txtPasajes = view.findViewById(R.id.txt_pasajes_anticipo);
        txtAlimentacion = view.findViewById(R.id.txt_alimentacion_anticipo);
        txtHotel = view.findViewById(R.id.txt_hotel_anticipo);
        txtMovilidad = view.findViewById(R.id.txt_movilidad_anticipo);
        progressBar = view.findViewById(R.id.progressBar_resumen);
        btnRegistrarAnticipo = view.findViewById(R.id.btn_anticipo_registrar);
        btnLimpiar = view.findViewById(R.id.btn_anticipo_limpiar);

        actvMotivoAnticipo = view.findViewById(R.id.actvMotivoAnticipo);
        actvSedeDestino = view.findViewById(R.id.actvSedeDestino);

        apiService = ApiAdapter.getApiService();

        btnRegistrarAnticipo.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        txtFechaInicio.setOnClickListener(this);
        txtFechaFin.setOnClickListener(this);

        //Seteando fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date actual = new Date();
        Date diaSiguente = new Date(actual.getTime() + (1000 * 60 * 60 * 24));
        txtFechaInicio.setText(sdf.format(actual));
        txtFechaFin.setText(sdf.format(diaSiguente));

        txtFechaInicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                actualizarCalendario();
            }
        });

        txtFechaFin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                actualizarCalendario();
            }
        });

        actvMotivoAnticipo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idMotivoSeleccionado = MotivoAnticipo.listaMotivos.get(i).getId();
            }
        });
        actvSedeDestino.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSedeSeleccionada = Sede.listaSedes.get(i).getId();
                resumenTarifas();
            }
        });

        validarPendientes();
        cargarMotivosAnticipo();
        cargarSedes();
        return view;
    }

    private void validarPendientes() {
        apiService.getValidacionAnticipo(DatosSesion.sesion.getToken(),DatosSesion.sesion.getId()).enqueue(new Callback<ValidacionResponse>() {
            @Override
            public void onResponse(Call<ValidacionResponse> call, Response<ValidacionResponse> response) {
                if (response.code() == 200) {
                    ValidacionResponse validacion = response.body();
                    boolean status = validacion.getStatus();
                    if (status) {
                        if (validacion.getData().getValidacion().equalsIgnoreCase("1")) {
                            Helper.mensajeInformacion(getActivity(),"INFO","Tiene 3 o más anticipos pendientes por rendicion por favor, regularizar");
                            btnRegistrarAnticipo.setEnabled(false);
                        }
                    }
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        String message =  jsonError.getString("message");
                        Log.e("ERROR VALIDANDO", message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ValidacionResponse> call, Throwable t) {
                Log.e("Error validando anticipo", t.getMessage());

            }
        });
    }

    //luego agregar el validar fecha inicio y fin
    private void actualizarCalendario(){
        diasAnticipo = Helper.diasEntreDosFechas(txtFechaInicio.getText().toString(),txtFechaFin.getText().toString());
        Toast.makeText(getActivity(), "DIAS DIFERENCIA " + diasAnticipo, Toast.LENGTH_SHORT).show();
        if (diasAnticipo<=0){
            Snackbar
                    .make(getActivity().findViewById(R.id.layout_solitcitud_anticipo), getContext().getResources().getString(R.string.validacion_fechas), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.error))
                    .show();
        }
        resumenTarifas();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_anticipo_registrar:
                Helper.mensajeConfirmacion(SolicitudAnticipoFragment.this.getActivity(),getString(R.string.confirmacion),getString(R.string.confirmacion_anticipo),getString(R.string.si),"NO",new TaskGrabarAnticipo());
                break;
            case R.id.btn_anticipo_limpiar:
                limpiar();
                break;
            case  R.id.txtFechaFin:
                Pickers.obtenerFecha(getActivity(),txtFechaFin);
                break;
            case R.id.txtFechaInicio:
                Pickers.obtenerFecha(getActivity(),txtFechaInicio);
                break;

        }
    }

    private void registrarAnticipo() {
        String descripcion = txtDescripcion.getText().toString();
        String fechaInicio = Helper.formatearDMA_to_AMD(txtFechaInicio.getText().toString());
        String fechaFin = Helper.formatearDMA_to_AMD(txtFechaFin.getText().toString());
//        Toast.makeText(getActivity(), fechaInicio, Toast.LENGTH_SHORT).show();
        if (validar()){
            progressBar.setVisibility(View.VISIBLE);
            apiService.getAnticipoRegistrado(DatosSesion.sesion.getToken(),descripcion,fechaInicio,fechaFin,idMotivoSeleccionado,idSedeSeleccionada,DatosSesion.sesion.getId()).enqueue(new Callback<AnticipoRegistroResponse>() {
                @Override
                public void onResponse(Call<AnticipoRegistroResponse> call, Response<AnticipoRegistroResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.code() == 201) {
                        AnticipoRegistroResponse anticipoResponse = response.body();
                        boolean status = anticipoResponse.getStatus();
                        if (status){
                            Anticipo anticipo = anticipoResponse.getData();
                            String anticipoGrabado = getResources().getString(R.string.anticipo_info)+ "\n\n" +
                                    getResources().getString(R.string.anticipo) + anticipo.getId() + "\n"+
                                    getResources().getString(R.string.descripci_n) + " : "+txtDescripcion.getText() + "\n" +
                                    getResources().getString(R.string.total_de_viaticos) + " : " +txtTotalViaticos.getText() + "\n"
                                    ;

                            Helper.mensajeInformacion(getActivity(),getResources().getString(R.string.anticipo),anticipoGrabado);
//                        Snackbar
//                                .make(getActivity().findViewById(R.id.layout_solitcitud_anticipo), "SE REGISTRO EL ANTICIPO "+anticipo.getId(), Snackbar.LENGTH_LONG)
//                                .setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.primaryColor))
//                                .show();
                            limpiar();
                        }
                    }  else {
                        try {
                            JSONObject jsonError = new JSONObject(response.errorBody().string());
                            String message =  jsonError.getString("message");
                            Snackbar
                                    .make(getActivity().findViewById(R.id.layout_solitcitud_anticipo), message, Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.error))
                                    .show();
                            Log.e("ERROR ---->",message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<AnticipoRegistroResponse> call, Throwable t) {
                    Log.e("Error registrando anticipo", t.getMessage());

                }
            });

        }
    }

    private void cargarMotivosAnticipo(){
        apiService.getMotivosAnticipos(DatosSesion.sesion.getToken()).enqueue(new Callback<MotivoAnticipoResponse>() {
            @Override
            public void onResponse(Call<MotivoAnticipoResponse> call, Response<MotivoAnticipoResponse> response) {
                if (response.code() == 200) {
                    MotivoAnticipoResponse motivoAnticipoResponse = response.body();
                    boolean status = motivoAnticipoResponse.getStatus();
                    if (status) {
                        List<MotivoAnticipo> motivoAnticipoList = motivoAnticipoResponse.getData();
                        //Enlazando lista motivos a clase
                        MotivoAnticipo.listaMotivos = motivoAnticipoList;
                        String motivosDescripcion[] = new String[motivoAnticipoList.size()];
                        for (int i = 0; i < motivoAnticipoList.size() ; i++) {
                            motivosDescripcion[i] = motivoAnticipoList.get(i).getDescripcion();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,motivosDescripcion);
                        actvMotivoAnticipo.setAdapter(adapter);
                    }
                } else {
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
            public void onFailure(Call<MotivoAnticipoResponse> call, Throwable t) {
                Log.e("Error cargando motivos anticipos", t.getMessage());
            }
        });

    }

    private void cargarSedes(){
        apiService.getSedes(DatosSesion.sesion.getToken()).enqueue(new Callback<SedesResponse>() {
            @Override
            public void onResponse(Call<SedesResponse> call, Response<SedesResponse> response) {
                if (response.code() == 200) {
                    SedesResponse sedesResponse = response.body();
                    boolean status = sedesResponse.getStatus();
                    if (status) {
                        List<Sede> sedeList = sedesResponse.getData();
                        Sede.listaSedes = sedeList;
                        String sedeDescripcion[] = new String[sedeList.size()];
                        for (int i = 0; i < sedeList.size() ; i++) {
                            sedeDescripcion[i] = sedeList.get(i).getNombre();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,sedeDescripcion);
                        actvSedeDestino.setAdapter(adapter);
                    }
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        String message =  jsonError.getString("message");
                        Log.e("MENSAJE RESUMEN TARIFAS", message);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SedesResponse> call, Throwable t) {
                Log.e("Error cargando sedes", t.getMessage());
            }
        });

    }



    private void resumenTarifas() {
        if (idMotivoSeleccionado>0 && idSedeSeleccionada>0 && diasAnticipo>0) {
            progressBar.setVisibility(View.VISIBLE);
            apiService.getViaticos(DatosSesion.sesion.getToken(),idSedeSeleccionada,diasAnticipo).enqueue(new Callback<TarifaResponse>() {
                @Override
                public void onResponse(Call<TarifaResponse> call, Response<TarifaResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.code() == 200) {
                        TarifaResponse tarifaResponse = response.body();
                        boolean status = tarifaResponse.getStatus();
                        if (status) {
                            double montoTotal=0;
                            double montoPasajes = 0,montoAlimentacion = 0,montoHotel = 0,montoMovilidad = 0;
                            List<Tarifa> tarifaList = tarifaResponse.getData();
                            for (Tarifa tarifa:tarifaList ) {
                                Toast.makeText(getContext(), "DDD  " + tarifa.getMonto_maximo(), Toast.LENGTH_SHORT).show();
//                                if (tarifa.getSe_calcula_por_dia().equals("1")) {
//                                    montoRubro = tarifa.getMonto_maximo() * diasAnticipo;
//                                } else {
//                                    montoRubro = tarifa.getMonto_maximo();
//                                }
                                switch (tarifa.getRubro_id()){
                                    case 1 :
                                        montoPasajes = tarifa.getMonto_maximo();
                                        break;
                                    case 2:
                                        montoAlimentacion = tarifa.getMonto_maximo();
                                        break;
                                    case 3 :
                                        montoHotel = tarifa.getMonto_maximo();
                                        break;
                                    case 4:
                                        montoMovilidad = tarifa.getMonto_maximo();
                                        break;
                                }

                            }
                            montoTotal = montoPasajes + montoAlimentacion + montoHotel + montoMovilidad;
                            txtPasajes.setText("S/. "+ montoPasajes);
                            txtAlimentacion.setText("S/. "+ montoAlimentacion);
                            txtHotel.setText("S/. "+ montoHotel);
                            txtMovilidad.setText("S/. "+ montoMovilidad);
                            txtTotalViaticos.setText("S./"+montoTotal);
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
                    Log.e("Error cargando resumen viaticos", t.getMessage());

                }
            });
        }
    }
    private boolean validar(){
        if (idMotivoSeleccionado==0) {
            Helper.mensajeInformacion(getContext(),"INFO",getContext().getResources().getString(R.string.validacion_motivo));
            return false;
        }

        if (txtDescripcion.getText().toString().equalsIgnoreCase("")) {
            Helper.mensajeInformacion(getContext(),"INFO",getContext().getResources().getString(R.string.validacion_descripcion));
            return false;
        }

        if (idSedeSeleccionada==0) {
            Helper.mensajeInformacion(getContext(),"INFO",getContext().getResources().getString(R.string.validacion_sede));
            return false;
        }

        if (diasAnticipo<=0){
            Helper.mensajeInformacion(getContext(),"INFO",getContext().getResources().getString(R.string.validacion_fechas));
            return false;

        }
        return true;
    }

    private void limpiar(){
        actvSedeDestino.setText("");
        actvMotivoAnticipo.setText("");
        txtDescripcion.setText("");
        txtPasajes.setText("");
        txtAlimentacion.setText("");
        txtHotel.setText("");
        txtMovilidad.setText("");
        txtTotalViaticos.setText("");
    }

    class TaskGrabarAnticipo implements Runnable{
        @Override
        public void run() {
            registrarAnticipo();
        }
    }
}