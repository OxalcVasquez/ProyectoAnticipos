package com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.presentation.adapter.RendicionAdapter;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.InformeGasto;

public class RendicionListadoFragment extends Fragment {

    RecyclerView recyclerRendicionGastos;
    RendicionAdapter rendicionAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rendicion_listado, container, false);

        this.getActivity().setTitle(getString(R.string.title_rendicion_gastos));

        recyclerRendicionGastos = view.findViewById(R.id.recycler_rendicion_gastos);
        recyclerRendicionGastos.setHasFixedSize(true);
        recyclerRendicionGastos.setLayoutManager(new LinearLayoutManager(this.getContext()));

        rendicionAdapter = new RendicionAdapter(this.getContext());
        recyclerRendicionGastos.setAdapter(rendicionAdapter);
        listar();

        return view;
    }

    private void listar() {
        new InformeGasto().cargarDatosRendicion();
        rendicionAdapter.cargarDatosRendicion(InformeGasto.listaInformeGasto);

    }
}