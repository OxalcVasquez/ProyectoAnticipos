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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarComprobanteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtFecha,txtRuc,txtDescripcion,txtSerie,txtCorrelativo,txtMontoComprobante;
    MaterialButton btnAgregar,btnAgregarFoto;
    AutoCompleteTextView actvRubro,actvComprobante;
    ImageView imgComprobante;
    ApiService apiService;
    public static final int REQUEST_PICK = 1;
    int idRubroSeleccionado, idTipoSeleccionado;
    String rubro, tipo;
    String imagePath;

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
                    imagePath = getPath(getApplicationContext(), rutaImagen);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        agregarComprobante();
                    }
                }
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
        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(
                MediaType.parse("image/*"),
                file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(
                "image",
                file.getName(),
                requestFile);

        apiService.subirImage(part).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("prueba", "Code: " + String.valueOf(response.code()));
                Log.d("prueba", "isSuccessful: " +  String.valueOf(response.isSuccessful()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

//        Comprobante comprobante = new Comprobante();
//        String ruc = txtRuc.getText().toString();
//        String descripcion = txtDescripcion.getText().toString();
//        String serie = txtSerie.getText().toString();
//        String correlativo = txtCorrelativo.getText().toString();
//        String montoTotal = txtMontoComprobante.getText().toString();
//        String fecha = Helper.formatearDMA_to_AMD(txtFecha.getText().toString());
//        comprobante.setRuc(ruc);
//        comprobante.setDescripcion(descripcion);
//        comprobante.setSerie(serie);
//        comprobante.setCorrelativo(correlativo);
//        comprobante.setMontoTotal(Double.parseDouble(montoTotal));
//        comprobante.setFechaEmision(fecha);
//        comprobante.setRubro(rubro);
//        comprobante.setRubroId(idRubroSeleccionado);
//        comprobante.setTipoComprobante(tipo);
//        comprobante.setTipoComprobanteId(idTipoSeleccionado);
//        comprobante.setFoto(file.getName());
//        Comprobante.comprobanteListado.add(comprobante);
//        this.finish();
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

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}