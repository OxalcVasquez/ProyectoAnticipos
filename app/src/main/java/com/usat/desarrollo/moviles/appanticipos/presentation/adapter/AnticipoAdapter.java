package com.usat.desarrollo.moviles.appanticipos.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usat.desarrollo.moviles.appanticipos.R;
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

        holder.txtAnticipo.setText(anticipo.getDescripcion());
        holder.txtFechaInicio.setText("Del: "+anticipo.getFecha_inicio());
        holder.txtFechaFin.setText("Al: "+anticipo.getFecha_fin());
        holder.txtEstado.setText(anticipo.getEstado());
        holder.txtMonto.setText("Monto: "+anticipo.getMonto_total());

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAnticipo = itemView.findViewById(R.id.txtAnticipo);
            txtMonto = itemView.findViewById(R.id.txtMonto);
            txtFechaInicio = itemView.findViewById(R.id.txtFechaInicio);
            txtFechaFin = itemView.findViewById(R.id.txtFechaFin);
            txtEstado = itemView.findViewById(R.id.txtEstado);


        }

        @Override
        public void onClick(View view) {

        }
    }

}
