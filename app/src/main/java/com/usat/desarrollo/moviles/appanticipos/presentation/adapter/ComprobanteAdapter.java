package com.usat.desarrollo.moviles.appanticipos.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Comprobante;

import java.util.ArrayList;

public class ComprobanteAdapter extends RecyclerView.Adapter<ComprobanteAdapter.ViewHolder> {

    public static  ArrayList<Comprobante> comprobanteList = new ArrayList<>();
    private Context context;

    public ComprobanteAdapter(Context context) {
        this.comprobanteList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comprobante_rendicion_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comprobante comprobante = comprobanteList.get(position);
        holder.txtRubro.setText(comprobante.getRubro());
        holder.txtFechaEmision.setText(comprobante.getFechaEmision());
        holder.txtTipo.setText(comprobante.getTipoComprobante());
        holder.txtTotal.setText("S/. "+comprobante.getMontoTotal());
        holder.txtComprobante.setText(comprobante.getSerie() + "-" +comprobante.getCorrelativo());

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

        TextView txtRubro, txtFechaEmision, txtComprobante, txtTotal, txtTipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRubro = itemView.findViewById(R.id.txt_rubro_listado);
            txtFechaEmision = itemView.findViewById(R.id.txt_fecha_emision_listado);
            txtComprobante = itemView.findViewById(R.id.txt_comprobante_listado);
            txtTipo = itemView.findViewById(R.id.txt_tipo_comprobante);
            txtTotal = itemView.findViewById(R.id.txt_factura_total_listado);
        }


    }

    public double[] calcularTotales(){
        double montoPasajes = 0,montoAlimentacion = 0,montoHotel = 0,montoMovilidad = 0,montoDevolucion = 0, total;
        for (Comprobante comprobante: comprobanteList) {
            switch (comprobante.getRubroId()){
                case 1 :
                    montoPasajes += comprobante.getMontoTotal();
                    break;
                case 2:
                    montoAlimentacion += comprobante.getMontoTotal();;
                    break;
                case 3 :
                    montoHotel += comprobante.getMontoTotal();;
                    break;
                case 4:
                    montoMovilidad += comprobante.getMontoTotal();;
                    break;
                case 5 :
                    montoDevolucion += comprobante.getMontoTotal();;
                    break;
            }
        }
        total = montoPasajes + montoAlimentacion + montoHotel + montoMovilidad;
        double montos [] = {montoPasajes,montoAlimentacion,montoHotel,montoMovilidad,montoDevolucion};
        return montos;
    }
}
