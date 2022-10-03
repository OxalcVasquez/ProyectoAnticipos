package com.usat.desarrollo.moviles.appanticipos;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class SolicitudAnticipoFragment extends Fragment implements View.OnClickListener{

    TextView txtDescripcion,txtFechaInicio,txtFechaFin ;
    MaterialButton btnAgregar,btnLimpiar;
    AutoCompleteTextView actvMotivoAnticipo, actvSedeDestino;

    Calendar calendar1 = Calendar.getInstance();
    Calendar calendar2 = Calendar.getInstance();




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



        btnAgregar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        txtFechaInicio.setOnClickListener(this);
        txtFechaFin.setOnClickListener(this);
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
            case R.id.btn_comprobante_agregar:
                //TODO AGREGAR
                break;
            case R.id.btn_comprobante_limpiar:
                //TODO LIMPIAR
                break;

            case R.id.txtFechaInicio:
                new DatePickerDialog(getActivity(),fecha1,calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
                case  R.id.txtFechaFin:
                    new DatePickerDialog(getActivity(),fecha2,calendar2.get(Calendar.YEAR),calendar2.get(Calendar.MONTH),
                            calendar2.get(Calendar.DAY_OF_MONTH)
                    ).show();
                    break;


        }
    }

}