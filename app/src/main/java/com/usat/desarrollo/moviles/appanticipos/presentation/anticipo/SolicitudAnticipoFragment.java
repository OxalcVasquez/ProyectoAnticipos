package com.usat.desarrollo.moviles.appanticipos.presentation.anticipo;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.MotivoAnticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Sede;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Tarifa;
import com.usat.desarrollo.moviles.appanticipos.utils.Helper;
import com.usat.desarrollo.moviles.appanticipos.utils.Pickers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SolicitudAnticipoFragment extends Fragment implements View.OnClickListener {

    TextView txtDescripcion,txtResumenSolicitud,txtTotalViaticos;
    EditText txtFechaInicio,txtFechaFin;
    MaterialButton btnRegistrarAnticipo,btnLimpiar;
    AutoCompleteTextView actvMotivoAnticipo, actvSedeDestino;
    ProgressBar progressBar;
    int idMotivoSeleccionado,idSedeSeleccionada;
    long diasAnticipo;
    String fechaInicio,fechaFin;

    Calendar calendar1 = Calendar.getInstance();
    Calendar calendar2 = Calendar.getInstance();

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
        txtResumenSolicitud = view.findViewById(R.id.txt_resumen_solicitud_anticipo);
        txtTotalViaticos = view.findViewById(R.id.txt_total_viaticos);
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


        cargarMotivosAnticipo();
        cargarSedes();
        return view;
    }

    DatePickerDialog.OnDateSetListener fecha1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
            calendar1.set(Calendar.YEAR,anio);
            calendar1.set(Calendar.MONTH,mes);
            calendar1.set(Calendar.DAY_OF_MONTH,dia);
            actualizarCalendario();

        }
    };

    DatePickerDialog.OnDateSetListener fecha2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
            calendar2.set(Calendar.YEAR,anio);
            calendar2.set(Calendar.MONTH,mes);
            calendar2.set(Calendar.DAY_OF_MONTH,dia);
            actualizarCalendario();

        }

    };
    //luego agregar el validar fecha inicio y fin
    private void actualizarCalendario(){
        String format = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        txtFechaInicio.setText(sdf.format(calendar1.getTime()));
        txtFechaFin.setText(sdf.format(calendar2.getTime()));
        String formatRegistro = "yyyy-MM-dd";
        SimpleDateFormat sdfRegistro = new SimpleDateFormat(formatRegistro, Locale.US);
        fechaInicio =sdfRegistro.format(calendar1.getTime());
        fechaFin =sdfRegistro.format(calendar2.getTime());
        //Para obtener dias de diferencia entre los anticipos
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDate fechaInicio = LocalDate.parse(txtFechaInicio.getText().toString(), dtf);
        LocalDate fechaFin = LocalDate.parse(txtFechaFin.getText().toString(), dtf);
        diasAnticipo = Duration.between(fechaInicio.atStartOfDay(), fechaFin.atStartOfDay()).toDays();

        if (diasAnticipo<=0){
            Snackbar
                    .make(getActivity().findViewById(R.id.layout_solitcitud_anticipo), "La fecha de fin debe ser mayor a la de inicio", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.error))
                    .show();
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_anticipo_registrar:
                registrarAnticipo();
                break;
            case R.id.btn_anticipo_limpiar:
                limpiar();
                break;
            case  R.id.txtFechaFin:
                Pickers.obtenerFecha(getActivity(),txtFechaFin);
//                new DatePickerDialog(getActivity(),fecha2,calendar2.get(Calendar.YEAR),calendar2.get(Calendar.MONTH),
//                        calendar2.get(Calendar.DAY_OF_MONTH)
//                ).show();
                break;
            case R.id.txtFechaInicio:
                Pickers.obtenerFecha(getActivity(),txtFechaInicio);
//
//                new DatePickerDialog(getActivity(),fecha1,calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),
//                        calendar1.get(Calendar.DAY_OF_MONTH)
//                ).show();
                break;

        }
    }

    private void registrarAnticipo() {
        progressBar.setVisibility(View.VISIBLE);
        String descripcion = txtDescripcion.getText().toString();

        apiService.getAnticipoRegistrado(DatosSesion.TOKEN,descripcion,fechaInicio,fechaFin,idMotivoSeleccionado,idSedeSeleccionada,DatosSesion.USUARIO_ID).enqueue(new Callback<AnticipoRegistroResponse>() {
            @Override
            public void onResponse(Call<AnticipoRegistroResponse> call, Response<AnticipoRegistroResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 201) {
                    AnticipoRegistroResponse anticipoResponse = response.body();
                    boolean status = anticipoResponse.getStatus();
                    if (status){
                        Anticipo anticipo = anticipoResponse.getData();
                        Snackbar
                                .make(getActivity().findViewById(R.id.layout_solitcitud_anticipo), "SE REGISTRO EL ANTICIPO "+anticipo.getAnticipo_id(), Snackbar.LENGTH_LONG)
                                .setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.primaryColor))
                                .show();
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

    private void cargarMotivosAnticipo(){
        apiService.getMotivosAnticipos(DatosSesion.TOKEN).enqueue(new Callback<MotivoAnticipoResponse>() {
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
        apiService.getSedes(DatosSesion.TOKEN).enqueue(new Callback<SedesResponse>() {
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
        progressBar.setVisibility(View.VISIBLE);
        apiService.getViaticos(DatosSesion.TOKEN,idSedeSeleccionada).enqueue(new Callback<TarifaResponse>() {
            @Override
            public void onResponse(Call<TarifaResponse> call, Response<TarifaResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    TarifaResponse tarifaResponse = response.body();
                    boolean status = tarifaResponse.getStatus();
                    if (status) {
                        String resumen = "";
                        float montoTotal=0;
                        float montoRubro =0;
                        List<Tarifa> tarifaList = tarifaResponse.getData();
                        for (Tarifa tarifa:tarifaList ) {
                            if (tarifa.getSe_calcula_por_dia().equals("1")) {
                                 montoRubro = tarifa.getMonto_maximo() * diasAnticipo;
                            } else {
                                 montoRubro = tarifa.getMonto_maximo();
                            }
                            montoTotal += montoRubro;
                            resumen += tarifa.getRubro() +": S./" + montoRubro + "\n";
                    }
                        txtResumenSolicitud.setText(resumen);
                        txtTotalViaticos.setText("TOTAL VIATICOS S./"+montoTotal);
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
    private void limpiar(){
        actvSedeDestino.setText("");
        actvMotivoAnticipo.setText("");
        txtFechaFin.setText("");
        txtFechaInicio.setText("");
        txtDescripcion.setText("");
        txtResumenSolicitud.setText("RESUMEN");
        txtTotalViaticos.setText("TOTAL VIÁTICOS");
    }
}