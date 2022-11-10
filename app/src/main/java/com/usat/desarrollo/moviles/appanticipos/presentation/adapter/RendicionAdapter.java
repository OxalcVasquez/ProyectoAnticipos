package com.usat.desarrollo.moviles.appanticipos.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.InformeGasto;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RendicionAdapter extends RecyclerView.Adapter<RendicionAdapter.ViewHolder> {

    private ArrayList<InformeGasto> rendicionList;
    private Context context;
    public int posItem;

    public RendicionAdapter(Context context) {
        this.rendicionList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rendicion_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mostrarRendicion(position);
    }

    @Override
    public int getItemCount() {
        return rendicionList.size();
    }

    public void cargarDatosRendicion(ArrayList<InformeGasto> listado) {
        rendicionList = listado;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile;
        TextView txtInforme, txtFechaRegistro, txtFechaDel, txtFechaAl, txtTotalRendido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile_docente);
            txtInforme = itemView.findViewById(R.id.txt_titulo_informe);
            txtFechaRegistro = itemView.findViewById(R.id.txt_fecha_registro_listado);
            txtFechaDel = itemView.findViewById(R.id.txt_fecha_del_listado);
            txtFechaAl = itemView.findViewById(R.id.txt_fecha_al_listado);
            txtTotalRendido = itemView.findViewById(R.id.txt_total_rendido_listado);
        }

        public void mostrarRendicion(int pos) {

        }
    }
}
