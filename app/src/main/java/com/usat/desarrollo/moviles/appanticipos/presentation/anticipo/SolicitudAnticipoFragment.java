package com.usat.desarrollo.moviles.appanticipos.presentation.anticipo;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.usat.desarrollo.moviles.appanticipos.LoginActivity;
import com.usat.desarrollo.moviles.appanticipos.MenuActivity;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.LoginResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.MotivoAnticipoResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.SedesResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.MotivoAnticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Sede;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SolicitudAnticipoFragment extends Fragment implements View.OnClickListener{

    TextView txtDescripcion,txtFechaInicio,txtFechaFin ;
    MaterialButton btnAgregar,btnLimpiar;
    AutoCompleteTextView actvMotivoAnticipo, actvSedeDestino;
    int idMotivoSeleccionado;

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
        btnAgregar = view.findViewById(R.id.btnRegistrarS);
        btnLimpiar = view.findViewById(R.id.btnLimpiarS);
        actvMotivoAnticipo = view.findViewById(R.id.actvMotivoAnticipo);
        actvSedeDestino = view.findViewById(R.id.actvSedeDestino);

        apiService = ApiAdapter.getApiService();

        btnAgregar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        txtFechaInicio.setOnClickListener(this);
        txtFechaFin.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case  R.id.txtFechaFin:
                new DatePickerDialog(getActivity(),fecha2,calendar2.get(Calendar.YEAR),calendar2.get(Calendar.MONTH),
                        calendar2.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
            case R.id.txtFechaInicio:
                new DatePickerDialog(getActivity(),fecha1,calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;

        }
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
                        String motivosDescripcion[] = new String[motivoAnticipoList.size()];
                        for (int i = 0; i < motivoAnticipoList.size() ; i++) {
                            motivosDescripcion[i] = motivoAnticipoList.get(i).toString();
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
                        String sedeDescripcion[] = new String[sedeList.size()];
                        for (int i = 0; i < sedeList.size() ; i++) {
                            sedeDescripcion[i] = sedeList.get(i).toString();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,sedeDescripcion);
                        actvSedeDestino.setAdapter(adapter);
                    }
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        String message =  jsonError.getString("message");
                        Log.e("ERROR CARGANDO LISTADO SEDES", message);

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

}