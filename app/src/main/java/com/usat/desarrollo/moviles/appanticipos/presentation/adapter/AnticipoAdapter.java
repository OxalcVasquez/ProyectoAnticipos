package com.usat.desarrollo.moviles.appanticipos.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;

import java.util.ArrayList;

public class AnticipoAdapter extends RecyclerView.Adapter<AnticipoAdapter.ViewHolder>{
    private Context context;
    public static ArrayList<Anticipo> listaAnticipo;

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

        holder.txtAnticipo.setText(""+ String.valueOf(anticipo.getDescripcion()));
        holder.txtFechaInicio.setText("Del: "+String.valueOf(anticipo.getFecha_inicio()));
        holder.txtFechaFin.setText("Al: "+String.valueOf(anticipo.getFecha_fin()));
        holder.txtEstado.setText(""+ String.valueOf(anticipo.getEstado()));
        holder.txtMonto.setText("Monto: "+ String.valueOf(anticipo.getMonto_total()));
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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


        }

        @Override
        public void onClick(View view) {

        }
    }

}
