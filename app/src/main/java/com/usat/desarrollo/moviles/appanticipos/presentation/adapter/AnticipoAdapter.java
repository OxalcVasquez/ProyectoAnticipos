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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.presentation.anticipo.AnticipoListadoFragment;
import com.usat.desarrollo.moviles.appanticipos.presentation.anticipo.SolicitudAnticipoFragment;

import java.util.ArrayList;

public class AnticipoAdapter extends RecyclerView.Adapter<AnticipoAdapter.ViewHolder>{
    private Context context;
    public static ArrayList<Anticipo> listaAnticipo;
    public int posicionItemSeleccionadoRecyclerView;

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

        @Override
        public void onClick(View view) {
            if(DatosSesion.sesion.getRol_id()==1){
                if(txtEstado.getText().toString().equalsIgnoreCase("OBSERVADO")){
                    //METELE OX
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if(DatosSesion.sesion.getRol_id() == 1){
            }else{
                if(DatosSesion.sesion.getRol_id() == 2){
                    contextMenu.setHeaderTitle("Opciones");
                    contextMenu.add(0, 1, 0, "Aprobar");
                    contextMenu.add(0, 2, 0, "Observar");
                    contextMenu.add(0, 3, 0, "Rechazar");

                }else{
                    contextMenu.setHeaderTitle("Opciones");
                    contextMenu.add(0, 1, 0, "Aprobar");
                    contextMenu.add(0, 2, 0, "Observar");

                }
            }

        }

        @Override
        public boolean onLongClick(View view) {
            //Permite obtener la posición del item seleccionado en el RecyclerView
            posicionItemSeleccionadoRecyclerView = this.getAdapterPosition();
            //Toast.makeText(context, ""+listaAnticipo.get(posicionItemSeleccionadoRecyclerView).getId(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
