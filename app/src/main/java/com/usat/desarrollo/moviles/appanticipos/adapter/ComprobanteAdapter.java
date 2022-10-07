package com.usat.desarrollo.moviles.appanticipos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.modelo.Comprobante;

import java.util.ArrayList;

public class ComprobanteAdapter extends RecyclerView.Adapter<ComprobanteAdapter.ViewHolder> {

    private ArrayList<Comprobante> comprobanteList;
    private Context context;

    public ComprobanteAdapter(Context context) {
        this.comprobanteList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comprobante_rendicion_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mostrarComprobante(position);
    }

    @Override
    public int getItemCount() {
        return comprobanteList.size();
    }

    public void cargarDatosComprobante(ArrayList<Comprobante> listado) {
        comprobanteList = listado;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtRubro, txtFechaEmision, txtComprobante, txtTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRubro = itemView.findViewById(R.id.txt_rubro_listado);
            txtFechaEmision = itemView.findViewById(R.id.txt_fecha_emision_listado);
            txtComprobante = itemView.findViewById(R.id.txt_comprobante_listado);
            txtTotal = itemView.findViewById(R.id.txt_factura_total_listado);
        }

        public void mostrarComprobante(int pos) {

        }
    }
}
