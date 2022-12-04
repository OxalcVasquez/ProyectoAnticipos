package com.usat.desarrollo.moviles.appanticipos.presentation.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.usat.desarrollo.moviles.appanticipos.presentation.anticipo.AnticipoListadoFragment;
import com.usat.desarrollo.moviles.appanticipos.presentation.anticipo.SolicitudAnticipoFragment;
import com.usat.desarrollo.moviles.appanticipos.utils.Helper;
import com.usat.desarrollo.moviles.appanticipos.utils.Pickers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnticipoAdapter extends RecyclerView.Adapter<AnticipoAdapter.ViewHolder>{
    private Context context;
    public static ArrayList<Anticipo> listaAnticipo;
    public int posicionItemSeleccionadoRecyclerView;
    int idMotivoSeleccionado,idSedeSeleccionada;
    int diasAnticipo=1;
    Anticipo anticipoSeleccionado;


    public AnticipoAdapter(Context context) {
        this.context = context;
        listaAnticipo = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anticipo_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Anticipo anticipo = listaAnticipo.get(position);

        holder.txtAnticipo.setText(" "+ String.valueOf(anticipo.getDescripcion()));
        holder.txtFechaInicio.setText(" "+String.valueOf(anticipo.getFecha_inicio()));
        holder.txtFechaFin.setText(" "+String.valueOf(anticipo.getFecha_fin()));
        //holder.txtEstado.setText(""+ String.valueOf(anticipo.getEstado()));
        holder.txtMonto.setText(" "+ String.valueOf(anticipo.getMonto_total()));

        switch (anticipo.getEstado()) {
            case "REGISTRADO":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.register));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_registrado));
                break;
            case "APROBADO":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.approved));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_aprobado));
                break;
            case "DESIGNADO":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.approved));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_designado));
                break;
            case "RECHAZADO":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.unapproved));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_rechazado));
                break;
            case "RENDIDO":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.register));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_rendido));
                break;
            case "OBSERVADO":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.warning));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_observado));
                break;
            case "PENDIENTE":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.secondaryDarkColor));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_pendiente));
                break;
            case "RENDICION R":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.unapproved));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_rendicionr));
                break;
            case "RENDICION A":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.approved));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_rendiciona));
                break;
            case "CERRADO":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.primaryTextColor));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_cerrado));
                break;
            case "SUBSANADO":
                holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.ic_launcher_background));
                holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_subsanado));
                break;
        }

        Glide.with(context)
                .load(ApiAdapter.BASE_URL.toString() + anticipo.getImg())
                .into(holder.imgSede);
    }

    @Override
    public int getItemCount() {
        return listaAnticipo.size();
    }

    public void cargarDatos(ArrayList<Anticipo> lista){
        listaAnticipo = lista;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener, View.OnLongClickListener{
        TextView txtAnticipo, txtMonto, txtFechaInicio, txtFechaFin, txtEstado;
        ImageView imgSede;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAnticipo = itemView.findViewById(R.id.txtAnticipo);
            txtMonto = itemView.findViewById(R.id.txtMonto);
            txtFechaInicio = itemView.findViewById(R.id.txtFechaInicio);
            txtFechaFin = itemView.findViewById(R.id.txtFechaFin);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            imgSede = itemView.findViewById(R.id.imgSede);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

        }
        //Para up[date
        TextView txtDescripcion , txtTotalViaticos,txtPasajes, txtAlimentacion, txtHotel, txtMovilidad;
        EditText txtFechaInicioDialog,txtFechaFinDialog;
        MaterialButton btnSubsanar,btnCerrarDialog;
        AutoCompleteTextView actvMotivoAnticipo, actvSedeDestino;
        ProgressBar progressBar;
        ApiService apiService = ApiAdapter.getApiService();

        @Override
        public void onClick(View view) {
            if(DatosSesion.sesion.getRol_id()==1){
                anticipoSeleccionado = listaAnticipo.get(posicionItemSeleccionadoRecyclerView);
                if(txtEstado.getText().toString().equalsIgnoreCase(context.getResources().getString(R.string.estado_observado))){
                    final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
                    dialog.setContentView(R.layout.dialog_anticipo);
                    dialog.setCancelable(true);

                    txtDescripcion = dialog.findViewById(R.id.txtDialogDescripcion);
                    txtDescripcion.setTextColor(ContextCompat.getColor(context,R.color.primaryDarkColor));
                    txtFechaInicioDialog = dialog.findViewById(R.id.txtDialogFechaInicio);
                    txtFechaInicioDialog.setTextColor(ContextCompat.getColor(context,R.color.primaryDarkColor));
                    txtFechaFinDialog = dialog.findViewById(R.id.txtDialogFechaFin);
                    txtFechaFinDialog.setTextColor(ContextCompat.getColor(context,R.color.primaryDarkColor));
                    txtTotalViaticos = dialog.findViewById(R.id.txt_dialog_total_viaticos);
                    txtPasajes = dialog.findViewById(R.id.txt_dialog_pasajes_anticipo);
                    txtAlimentacion = dialog.findViewById(R.id.txt_dialog_alimentacion_anticipo);
                    txtHotel = dialog.findViewById(R.id.txt_dialog_hotel_anticipo);
                    txtMovilidad = dialog.findViewById(R.id.txt_dialog_movilidad_anticipo);
                    progressBar = dialog.findViewById(R.id.progress_bar_dialog_anticipo);
                    btnSubsanar = dialog.findViewById(R.id.btn_anticipo_subsanar);
                    btnSubsanar.setBackgroundColor(ContextCompat.getColor(context,R.color.primaryColor));
                    btnCerrarDialog = dialog.findViewById(R.id.btn_subsanar_cerrar);

                    actvMotivoAnticipo = dialog.findViewById(R.id.actvDialogMotivoAnticipo);
                    actvMotivoAnticipo.setTextColor(ContextCompat.getColor(context,R.color.primaryDarkColor));
                    actvSedeDestino = dialog.findViewById(R.id.actvDialogSedeDestino);
                    actvSedeDestino.setTextColor(ContextCompat.getColor(context,R.color.primaryDarkColor));

                    //Seteando fecha actual
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date actual = new Date();
                    Date diaSiguente = new Date(actual.getTime() + (1000 * 60 * 60 * 24));
                    txtFechaInicioDialog.setText(sdf.format(actual));
                    txtFechaFinDialog.setText(sdf.format(diaSiguente));

                    txtFechaInicioDialog.addTextChangedListener(new TextWatcher() {
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

                    txtFechaFinDialog.addTextChangedListener(new TextWatcher() {
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

                    txtFechaFinDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Pickers.obtenerFecha(context,txtFechaFinDialog);

                        }
                    });
                    txtFechaInicioDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Pickers.obtenerFecha(context,txtFechaInicioDialog);

                        }
                    });
                    cargarMotivosAnticipo();
                    cargarSedes();
                    btnSubsanar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Helper.mensajeConfirmacion(context,context.getResources().getString(R.string.confirmacion),context.getResources().getString(R.string.confirmacion_anticipo),context.getString(R.string.si),"NO",new TaskSubsanarAnticipo());

                        }

                        private void subsanarAnticipo() {
                            String descripcion = txtDescripcion.getText().toString();
                            String fechaInicio = Helper.formatearDMA_to_AMD(txtFechaInicioDialog.getText().toString());
                            String fechaFin = Helper.formatearDMA_to_AMD(txtFechaFinDialog.getText().toString());
                            if (validar()){
                                progressBar.setVisibility(View.VISIBLE);
                                apiService.getAnticipoSubsanado(DatosSesion.sesion.getToken(),descripcion,fechaInicio,fechaFin,idMotivoSeleccionado,idSedeSeleccionada,anticipoSeleccionado.getId()).enqueue(new Callback<AnticipoRegistroResponse>() {
                                    @Override
                                    public void onResponse(Call<AnticipoRegistroResponse> call, Response<AnticipoRegistroResponse> response) {
                                        progressBar.setVisibility(View.GONE);
                                        if (response.code() == 200) {
                                            AnticipoRegistroResponse anticipoResponse = response.body();
                                            boolean status = anticipoResponse.getStatus();
                                            if (status){
                                                Anticipo anticipo = anticipoResponse.getData();
                                                String anticipoGrabado = context.getResources().getString(R.string.anticipo_info)+ "\n\n" +
                                                        context.getResources().getString(R.string.anticipo) + anticipo.getId() + "\n"+
                                                        context.getResources().getString(R.string.descripci_n) + " : "+txtDescripcion.getText() + "\n" +
                                                        context.getResources().getString(R.string.total_de_viaticos) + " : " +txtTotalViaticos.getText() + "\n"
                                                        ;

                                                Helper.mensajeInformacion(context,context.getResources().getString(R.string.anticipo),anticipoGrabado);
                                                notifyDataSetChanged();
                                                dialog.dismiss();
                                            }
                                        }  else {
                                            try {
                                                JSONObject jsonError = new JSONObject(response.errorBody().string());
                                                String message =  jsonError.getString("message");
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                                        Log.e("Error subsnanado anticipo", t.getMessage());

                                    }
                                });

                            }
                        }

                        class TaskSubsanarAnticipo implements Runnable{
                            @Override
                            public void run() {
                                subsanarAnticipo();
                            }
                        }
                    });

                    btnCerrarDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            }
        }



        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle(context.getResources().getString(R.string.opcion));

            if(DatosSesion.sesion.getRol_id() == 1){
                contextMenu.add(0,1,0,context.getResources().getString(R.string.ver_instancia));

            }else{
                if(DatosSesion.sesion.getRol_id() == 2){
                    contextMenu.add(0, 1, 0, "Aprobar");
                    contextMenu.add(0, 2, 0, "Observar");
                    contextMenu.add(0, 3, 0, "Rechazar");

                }else{
                    contextMenu.add(0, 1, 0, "Aprobar");
                    contextMenu.add(0, 2, 0, "Observar");

                }
            }

        }

        @Override
        public boolean onLongClick(View view) {
            //Permite obtener la posición del item seleccionado en el RecyclerView
            posicionItemSeleccionadoRecyclerView = this.getAdapterPosition();
            int posicionlista = listaAnticipo.get(posicionItemSeleccionadoRecyclerView).getId();
            anticipoSeleccionado = listaAnticipo.get(posicionItemSeleccionadoRecyclerView);
            //Toast.makeText(context, ""+listaAnticipo.get(posicionItemSeleccionadoRecyclerView).getId(), Toast.LENGTH_SHORT).show();
            return false;
        }

        private void actualizarCalendario(){
            diasAnticipo = Helper.diasEntreDosFechas(txtFechaInicioDialog.getText().toString(),txtFechaFinDialog.getText().toString());
            Toast.makeText(context, "DIAS DIFERENCIA " + diasAnticipo, Toast.LENGTH_SHORT).show();
            if (diasAnticipo<=0){
                Toast.makeText(context, context.getResources().getString(R.string.validacion_fechas), Toast.LENGTH_SHORT).show();

            }
            resumenTarifas();

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
                            if (context != null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,motivosDescripcion);
                                actvMotivoAnticipo.setAdapter(adapter);
                            }

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

                            if (context != null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,sedeDescripcion);
                                actvSedeDestino.setAdapter(adapter);
                            }
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
                Helper.mensajeInformacion(context,"INFO",context.getResources().getString(R.string.validacion_motivo));
                return false;
            }

            if (txtDescripcion.getText().toString().equalsIgnoreCase("")) {
                Helper.mensajeInformacion(context,"INFO",context.getResources().getString(R.string.validacion_descripcion));
                return false;
            }

            if (idSedeSeleccionada==0) {
                Helper.mensajeInformacion(context,"INFO",context.getResources().getString(R.string.validacion_sede));
                return false;
            }

            if (diasAnticipo<=0){
                Helper.mensajeInformacion(context,"INFO",context.getResources().getString(R.string.validacion_fechas));
                return false;

            }
            return true;
        }
    }

}
