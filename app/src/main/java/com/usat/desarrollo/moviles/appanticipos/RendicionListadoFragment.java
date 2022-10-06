package com.usat.desarrollo.moviles.appanticipos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RendicionListadoFragment extends Fragment {

    RecyclerView recyclerRendicionGastos;

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

        return view;
    }
}