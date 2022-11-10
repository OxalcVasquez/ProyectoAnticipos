package com.usat.desarrollo.moviles.appanticipos.presentation.anticipo;

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


public class AnticipoFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomAnticipo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anticipo, container, false);
        bottomAnticipo = view.findViewById(R.id.bottom_menu_anticipos);

        bottomAnticipo.setOnNavigationItemSelectedListener(this);
        this.onNavigationItemSelected(bottomAnticipo.getMenu().getItem(0));
        return view;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = new Fragment();
        switch (id) {
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
    }

}