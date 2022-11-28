package com.usat.desarrollo.moviles.appanticipos.presentation.comprobante;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.RubrosResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.TipoComprobanteResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Rubro;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.TipoComprobante;
import com.usat.desarrollo.moviles.appanticipos.utils.Gallery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarComprobanteActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtFecha,txtRuc,txtDescripcion,txtSerie,txtCorrelativo,txtMontoComprobante;
    MaterialButton btnAgregar,btnLimpiar,btnAgregarFoto;
    AutoCompleteTextView actvRubro,actvComprobante;
    ImageView imgComprobante;
    ApiService apiService;
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

        //Inicializando api service
        apiService = ApiAdapter.getApiService();

        btnAgregarFoto.setOnClickListener(this);
        btnAgregar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        txtFecha.setOnClickListener(this);

        //Cargando datos de autocompletextview
        cargarRubros();
        cargarTipoComprobante();
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

    private void cargarRubros() {
        apiService.getRubros(DatosSesion.sesion.getToken()).enqueue(new Callback<RubrosResponse>() {
            @Override
            public void onResponse(Call<RubrosResponse> call, Response<RubrosResponse> response) {
                if (response.code() == 200) {
                    RubrosResponse rubrosResponse = response.body();
                    boolean status = rubrosResponse.getStatus();
                    if (status) {
                        List<Rubro> rubroList = rubrosResponse.getData();
                        Rubro.listaRubros = rubroList;
                        String nombreRubros[] = new String[rubroList.size()];
                        for (int i = 0; i < rubroList.size(); i++) {
                            nombreRubros[i] = rubroList.get(i).getNombre();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarComprobanteActivity.this, android.R.layout.simple_dropdown_item_1line, nombreRubros);
                        actvRubro.setAdapter(adapter);
                    }

                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        String message = jsonError.getString("message");
                        Log.e("ERROR CARGANDO LISTADO RUBROS", message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<RubrosResponse> call, Throwable t) {
                Log.e("Error cargando  rubros", t.getMessage());

            }
        });
    }

        private void cargarTipoComprobante() {
            apiService.getTiposComprobante(DatosSesion.sesion.getToken()).enqueue(new Callback<TipoComprobanteResponse>() {
                @Override
                public void onResponse(Call<TipoComprobanteResponse> call, Response<TipoComprobanteResponse> response) {
                    if (response.code() == 200) {
                        TipoComprobanteResponse tipoComprobanteResponse = response.body();
                        boolean status = tipoComprobanteResponse.getStatus();
                        if (status) {
                            List<TipoComprobante> tipoComprobanteList = tipoComprobanteResponse.getData();
                            TipoComprobante.listaComprobamte = tipoComprobanteList;
                            String tipos[] = new String[tipoComprobanteList.size()];
                            for (int i = 0; i < tipoComprobanteList.size(); i++) {
                                tipos[i] = tipoComprobanteList.get(i).getNombre();
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarComprobanteActivity.this, android.R.layout.simple_dropdown_item_1line, tipos);
                            actvComprobante.setAdapter(adapter);
                        }

                    } else {
                        try {
                            JSONObject jsonError = new JSONObject(response.errorBody().string());
                            String message = jsonError.getString("message");
                            Log.e("ERROR CARGANDO LISTADO RUBROS", message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<TipoComprobanteResponse> call, Throwable t) {
                    Log.e("Error cargando  rubros", t.getMessage());

                }
            });

    }

}