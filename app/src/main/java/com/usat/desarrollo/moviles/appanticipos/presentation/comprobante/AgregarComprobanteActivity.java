package com.usat.desarrollo.moviles.appanticipos.presentation.comprobante;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.RubrosResponse;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.TipoComprobanteResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Comprobante;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Rubro;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.TipoComprobante;
import com.usat.desarrollo.moviles.appanticipos.utils.Gallery;
import com.usat.desarrollo.moviles.appanticipos.utils.Helper;
import com.usat.desarrollo.moviles.appanticipos.utils.Pickers;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarComprobanteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtFecha,txtRuc,txtDescripcion,txtSerie,txtCorrelativo,txtMontoComprobante,txtNumeroOperacion;
    MaterialButton btnAgregar,btnAgregarFoto;
    AutoCompleteTextView actvRubro,actvComprobante;
    ImageView imgComprobante;
    ApiService apiService;
    public static final int REQUEST_PICK = 1;
    int idRubroSeleccionado, idTipoSeleccionado;
    String rubro, tipo;
    String imageBASE64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_comprobante);
        this.setTitle(getString(R.string.btn_agregar_comprobante));
        txtFecha = findViewById(R.id.txt_fecha_comprobante);
        txtRuc = findViewById(R.id.txt_ruc_comprobante);
        txtDescripcion = findViewById(R.id.txt_descripcion_comprobante);
        txtSerie = findViewById(R.id.txt_serie_comprobante);
        txtCorrelativo = findViewById(R.id.txt_correlativo_comprobante);
        btnAgregar = findViewById(R.id.btn_comprobante_agregar);
        txtNumeroOperacion = findViewById(R.id.txt_num_operacion);
        txtMontoComprobante = findViewById(R.id.txt_monto_comprobante);
        actvComprobante = findViewById(R.id.actv_comprobante);
        actvRubro = findViewById(R.id.actv_comprobante_rubro);
        btnAgregarFoto = findViewById(R.id.btn_comprobante_foto);
        imgComprobante = findViewById(R.id.img_comprobante);

        //Inicializando api service
        apiService = ApiAdapter.getApiService();

        btnAgregarFoto.setOnClickListener(this);
        btnAgregar.setOnClickListener(this);
        txtFecha.setOnClickListener(this);

        //Cargando datos de autocompletextview
        cargarRubros();
        cargarTipoComprobante();

        //Para obtener id cada vez que sse cambie
        actvComprobante.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idTipoSeleccionado = TipoComprobante.listaComprobamte.get(i).getId();
                tipo = adapterView.getItemAtPosition(i).toString();
                if (idTipoSeleccionado == 3){
                    txtNumeroOperacion.setVisibility(View.VISIBLE);
                    txtRuc.setVisibility(View.GONE);
                    txtSerie.setVisibility(View.GONE);
                    txtCorrelativo.setVisibility(View.GONE);
                } else {
                    txtNumeroOperacion.setVisibility(View.GONE);
                    txtRuc.setVisibility(View.VISIBLE);
                    txtSerie.setVisibility(View.VISIBLE);
                    txtCorrelativo.setVisibility(View.VISIBLE);
                }
            }
        });

        actvRubro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idRubroSeleccionado = Rubro.listaRubros.get(i).getId();
                rubro = adapterView.getItemAtPosition(i).toString();
            }
        });


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
                    imageBASE64 = Helper.imageToBase64(bitmapCompress);
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
                agregarComprobante();
                break;
            case R.id.btn_comprobante_foto:
                abrirGaleria();
                break;
            case R.id.txt_fecha_comprobante:
                Pickers.obtenerFecha(AgregarComprobanteActivity.this,txtFecha);
                break;
        }
    }

    private void agregarComprobante() {
        if (validar()){
            Comprobante comprobante = new Comprobante();
            String ruc = txtRuc.getText().toString();
            String descripcion = txtDescripcion.getText().toString();
            String serie = txtSerie.getText().toString();
            String correlativo = txtCorrelativo.getText().toString();
            String montoTotal = txtMontoComprobante.getText().toString();
            String numOperacion = txtNumeroOperacion.getText().toString();
            String fecha = Helper.formatearDMA_to_AMD(txtFecha.getText().toString());
            comprobante.setRuc(ruc);
            comprobante.setDescripcion(descripcion);
            comprobante.setSerie(serie);
            comprobante.setCorrelativo(correlativo);
            comprobante.setMontoTotal(Double.parseDouble(montoTotal));
            comprobante.setFechaEmision(fecha);
            comprobante.setRubro(rubro);
            comprobante.setRubroId(idRubroSeleccionado);
            comprobante.setTipoComprobante(tipo);
            comprobante.setTipoComprobanteId(idTipoSeleccionado);
            comprobante.setFoto(imageBASE64);
            comprobante.setNumOperacion(numOperacion);
            Comprobante.comprobanteListado.add(comprobante);
            this.finish();
        }
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
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
                            if (rubroList.get(i).getNombre().equalsIgnoreCase("ALIMENTACION")) {
                                nombreRubros[i] = getResources().getString(R.string.alimentacion_rendicion);
                            } else if (rubroList.get(i).getNombre().equalsIgnoreCase("HOTEL")){
                                nombreRubros[i] = getResources().getString(R.string.hotel_rendicion);
                            } else if (rubroList.get(i).getNombre().equalsIgnoreCase("MOVILIDAD INTERNA")){
                                nombreRubros[i] = getResources().getString(R.string.movilidad_interna_rendicion);
                            } else if (rubroList.get(i).getNombre().equalsIgnoreCase("PASAJES TERRESTRES")){
                                nombreRubros[i] = getResources().getString(R.string.pasajes_terrestres_rendicion);
                            } else {
                                nombreRubros[i] = getResources().getString(R.string.devolucion);
                            }
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
                                if (tipoComprobanteList.get(i).getNombre().equalsIgnoreCase("BOLETA")) {
                                    tipos[i] = getResources().getString(R.string.boleta);
                                } else if (tipoComprobanteList.get(i).getNombre().equalsIgnoreCase("FACTURA")){
                                    tipos[i] = getResources().getString(R.string.factura);

                                } else {
                                    tipos[i] = getResources().getString(R.string.voucher);

                                }
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

    private boolean validar(){
        if (idRubroSeleccionado==0) {
            Helper.mensajeInformacion(this,"INFO",getResources().getString(R.string.validacion_rubro));
            return false;
        }
        if (idTipoSeleccionado==0) {
            Helper.mensajeInformacion(this,"INFO",getResources().getString(R.string.validacion_tipo));
            return false;
        }
        if (txtNumeroOperacion.getText().toString().equalsIgnoreCase("") && idTipoSeleccionado == 3){
            Helper.mensajeInformacion(this,"INFO",getResources().getString(R.string.validacion_num_operacion));
            return false;
        }

        if (txtRuc.getText().toString().equalsIgnoreCase("") && idTipoSeleccionado != 3) {
            Helper.mensajeInformacion(this,"INFO",getResources().getString(R.string.validacion_ruc));
            return false;
        }

        if (txtDescripcion.getText().toString().equalsIgnoreCase("")) {
            Helper.mensajeInformacion(this,"INFO",getResources().getString(R.string.validacion_descripcion));
            return false;
        }

        if (txtSerie.getText().toString().equalsIgnoreCase("") && idTipoSeleccionado != 3) {
            Helper.mensajeInformacion(this,"INFO",getResources().getString(R.string.validacion_serie));
            return false;
        }

        if (txtCorrelativo.getText().toString().equalsIgnoreCase("") && idTipoSeleccionado != 3) {
            Helper.mensajeInformacion(this,"INFO",getResources().getString(R.string.validacion_correlativo));
            return false;
        }

        if (txtFecha.getText().toString().equalsIgnoreCase("")) {
            Helper.mensajeInformacion(this,"INFO",getResources().getString(R.string.validacion_emision));
            return false;
        }

        if (txtMontoComprobante.getText().toString().equalsIgnoreCase("")) {
            Helper.mensajeInformacion(this,"INFO",getResources().getString(R.string.validacion_monto));
            return false;
        }

        if (imageBASE64 == null) {
            Helper.mensajeInformacion(this,"INFO",getResources().getString(R.string.validacion_foto));
            return false;
        }


        return true;
    }
}