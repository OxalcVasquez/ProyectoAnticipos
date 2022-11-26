package com.usat.desarrollo.moviles.appanticipos.presentation.anticipo;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.usat.desarrollo.moviles.appanticipos.R;


public class AnticipoFragment extends Fragment {

    BottomNavigationView bottomAnticipo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anticipo, container, false);
        bottomAnticipo = view.findViewById(R.id.bottom_menu_anticipos);

        bottomAnticipo.setOnItemSelectedListener(item -> {
            Fragment fragment = new Fragment();
            switch (item.getItemId()) {
                case R.id.opcion_solicitar_anticipo:
                    fragment = new SolicitudAnticipoFragment();
                    break;

                case R.id.opcion_listar_anticipos:
                    fragment = new AnticipoListadoFragment();
                    break;

            }

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contenedor_anticipos,fragment).commit();
            return true;
        });
        bottomAnticipo.setSelectedItemId(R.id.opcion_solicitar_anticipo);
        return view;
    }



}