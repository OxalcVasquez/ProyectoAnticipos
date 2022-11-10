package com.usat.desarrollo.moviles.appanticipos.presentation.comprobante;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.utils.Gallery;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AgregarComprobanteFragment extends Fragment implements View.OnClickListener {

    TextView txtFecha,txtRuc,txtDescripcion,txtSerie,txtCorrelativo,txtMontoComprobante;
    MaterialButton btnAgregar,btnLimpiar,btnAgregarFoto;
    AutoCompleteTextView actvRubro,actvComprobante;
    ImageView imgComprobante;
    Calendar calendar = Calendar.getInstance();
    public static final int REQUEST_PICK = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agregar_comprobante, container, false);
        getActivity().setTitle("Agregar comprobante");
        txtFecha = view.findViewById(R.id.txt_fecha_comprobante);
        txtRuc = view.findViewById(R.id.txt_ruc_comprobante);
        txtDescripcion = view.findViewById(R.id.txt_descripcion_comprobante);
        txtSerie = view.findViewById(R.id.txt_serie_comprobante);
        txtCorrelativo = view.findViewById(R.id.txt_correlativo_comprobante);
        btnAgregar = view.findViewById(R.id.btn_comprobante_agregar);
        btnLimpiar = view.findViewById(R.id.btn_comprobante_limpiar);
        txtMontoComprobante = view.findViewById(R.id.txt_monto_comprobante);
        actvComprobante = view.findViewById(R.id.actv_comprobante);
        actvRubro = view.findViewById(R.id.actv_comprobante_rubro);
        btnAgregarFoto = view.findViewById(R.id.btn_comprobante_foto);
        imgComprobante = view.findViewById(R.id.img_comprobante);


        btnAgregarFoto.setOnClickListener(this);
        btnAgregar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        txtFecha.setOnClickListener(this);
        return view;
    }

    DatePickerDialog.OnDateSetListener fecha = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
            calendar.set(Calendar.YEAR,anio);
            calendar.set(Calendar.MONTH,mes);
            calendar.set(Calendar.DAY_OF_MONTH,dia);
            actualizarCalendario();

        }
    };

    private void actualizarCalendario(){
        String format = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        txtFecha.setText(sdf.format(calendar.getTime()));

    }

    private void abrirGaleria() {
        startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),REQUEST_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK){
            if(resultCode == Activity.RESULT_OK){
                try {
                    Uri rutaImagen = data.getData();
                    Bitmap bitmap = Gallery.rotateImage(getActivity(),rutaImagen,Gallery.getOrientation(getActivity(),rutaImagen));
                    Bitmap bitmapCompress = Gallery.compress(bitmap);
                    imgComprobante.setImageBitmap(bitmapCompress);
                    imgComprobante.setTag("foto_real");
                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
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
            case R.id.btn_comprobante_foto:
                abrirGaleria();
                break;
            case R.id.txt_fecha_comprobante:
                new DatePickerDialog(getActivity(),fecha,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
        }
    }
}