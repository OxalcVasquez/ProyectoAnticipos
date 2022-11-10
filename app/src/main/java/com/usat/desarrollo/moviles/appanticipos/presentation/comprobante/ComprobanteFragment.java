package com.usat.desarrollo.moviles.appanticipos.presentation.comprobante;

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


public class ComprobanteFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomComprobante;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comprobante, container, false);
        bottomComprobante = view.findViewById(R.id.bottom_menu_comprobantes);

        bottomComprobante.setOnNavigationItemSelectedListener(this);
        this.onNavigationItemSelected(bottomComprobante.getMenu().getItem(0));
        return view;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = new Fragment();
        switch (id) {
            case R.id.opcion_agregar_comprobante:
                fragment = new AgregarComprobanteFragment();
                break;
        }

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedor_comprobantes,fragment).commit();
        return true;
    }


}