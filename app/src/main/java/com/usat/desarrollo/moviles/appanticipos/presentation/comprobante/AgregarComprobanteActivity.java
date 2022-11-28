package com.usat.desarrollo.moviles.appanticipos.presentation.comprobante;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.utils.Gallery;

import java.io.IOException;
import java.util.Calendar;

public class AgregarComprobanteActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtFecha,txtRuc,txtDescripcion,txtSerie,txtCorrelativo,txtMontoComprobante;
    MaterialButton btnAgregar,btnLimpiar,btnAgregarFoto;
    AutoCompleteTextView actvRubro,actvComprobante;
    ImageView imgComprobante;
    Calendar calendar = Calendar.getInstance();
    public static final int REQUEST_PICK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_comprobante);
        this.setTitle("Agregar comprobante");
        txtFecha = findViewById(R.id.txt_fecha_comprobante);
        txtRuc = findViewById(R.id.txt_ruc_comprobante);
        txtDescripcion = findViewById(R.id.txt_descripcion_comprobante);
        txtSerie = findViewById(R.id.txt_serie_comprobante);
        txtCorrelativo = findViewById(R.id.txt_correlativo_comprobante);
        btnAgregar = findViewById(R.id.btn_comprobante_agregar);
        btnLimpiar =findViewById(R.id.btn_comprobante_limpiar);
        txtMontoComprobante = findViewById(R.id.txt_monto_comprobante);
        actvComprobante = findViewById(R.id.actv_comprobante);
        actvRubro = findViewById(R.id.actv_comprobante_rubro);
        btnAgregarFoto = findViewById(R.id.btn_comprobante_foto);
        imgComprobante = findViewById(R.id.img_comprobante);


        btnAgregarFoto.setOnClickListener(this);
        btnAgregar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        txtFecha.setOnClickListener(this);
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
                    Bitmap bitmap = Gallery.rotateImage(AgregarComprobanteActivity.this,rutaImagen,Gallery.getOrientation(AgregarComprobanteActivity.this,rutaImagen));
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
                //TODO CALENDAR
                break;
        }
    }


}