package com.usat.desarrollo.moviles.appanticipos.presentation.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.HistorialAnticipoResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.HistorialAnticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.InformeGasto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RendicionAdapter extends RecyclerView.Adapter<RendicionAdapter.ViewHolder> {

    private Context context;
    public static ArrayList<InformeGasto> listadoInformes = new ArrayList<>();
    public int itemSeleccionado;
    ApiService apiService;

    public RendicionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rendicion_cardview, parent, false);
        apiService = ApiAdapter.getApiService();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InformeGasto informeGasto = listadoInformes.get(position);
        holder.txtInforme.setText(informeGasto.getNumInforme());
        holder.txtFechaRegistro.setText(informeGasto.getFechaRegistro());
        holder.txtFechaAl.setText(informeGasto.getFechaInicion());
        holder.txtAnticipo.setText(informeGasto.getAnticipo());
        holder.txtFechaDel.setText(informeGasto.getFechaFin());

        if (informeGasto.getEstado().equalsIgnoreCase("REGISTRADO")) {
            holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.register));
            holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_registrado));

        } else if (informeGasto.getEstado().equalsIgnoreCase("RENDICION A")) {
            holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.approved));
            holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_aprobado));

        } else {
            holder.txtEstado.setTextColor(ContextCompat.getColor(this.context,R.color.unapproved));
            holder.txtEstado.setText(this.context.getResources().getString(R.string.estado_rechazado));

        }
        holder.txtTotalRendido.setText("S/." + informeGasto.getTotalRendido());

    }

    @Override
    public int getItemCount() {
        return listadoInformes.size();
    }

    public void cargarDatosRendicion(ArrayList<InformeGasto> listado) {
        listadoInformes = listado;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener, View.OnCreateContextMenuListener {

        ImageView profile;
        TextView txtInforme, txtFechaRegistro, txtFechaDel, txtFechaAl, txtTotalRendido,txtAnticipo,txtEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile_docente);
            txtInforme = itemView.findViewById(R.id.txt_titulo_informe);
            txtFechaRegistro = itemView.findViewById(R.id.txt_fecha_registro_listado);
            txtFechaDel = itemView.findViewById(R.id.txt_fecha_del_listado);
            txtAnticipo = itemView.findViewById(R.id.txt_anticipo);
            txtEstado = itemView.findViewById(R.id.txt_estado_rendicion);
            txtFechaAl = itemView.findViewById(R.id.txt_fecha_al_listado);
            txtTotalRendido = itemView.findViewById(R.id.txt_total_rendido_listado);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public boolean onLongClick(View view) {
            itemSeleccionado = this.getAdapterPosition();
            return false;
        }

        @Override
        public void onClick(View view) {
//            final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
//            dialog.setContentView(R.layout.dialog_historial_informe);
//            dialog.setCancelable(true);
//
//            //Configure controls
//            TextView txtInstanciaInforme = dialog.findViewById(R.id.txt_instancia_informe);
//            TextView txtEvaluador = dialog.findViewById(R.id.txt_dialog_evaluador);
//            TextView txtEstado = dialog.findViewById(R.id.txt_estado_rendicion);
//
//            HistorialAnticipo historialAnticipo = HistorialAnticipo.listaHistorial.get(HistorialAnticipo.listaHistorial.size()-1);
//            txtInstanciaInforme.setText(historialAnticipo.getInstancia());
//            txtEvaluador.setText(historialAnticipo.getEvaluador());
//            txtEstado.setText(historialAnticipo.getEstado());
//
//            dialog.show();

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle(context.getResources().getString(R.string.opcion));
            if (DatosSesion.sesion.getRol_id() == 1) {
                contextMenu.add(0,1,0,context.getResources().getString(R.string.ver_instancia));
                if (txtEstado.getText().toString().equalsIgnoreCase(context.getResources().getString(R.string.estado_rechazado))) {
                    contextMenu.add(0,2,0,"Subsanar");
                }
            }
        }
    }
}
