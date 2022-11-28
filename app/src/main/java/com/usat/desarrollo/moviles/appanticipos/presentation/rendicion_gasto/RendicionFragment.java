package com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.usat.desarrollo.moviles.appanticipos.R;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto.RendicionGastosFragment;
import com.usat.desarrollo.moviles.appanticipos.presentation.rendicion_gasto.RendicionListadoFragment;

public class RendicionFragment extends Fragment {

    BottomNavigationView bottomNavigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_rendicion, container, false);
        bottomNavigation = view.findViewById(R.id.bottom_menu_rendicion);

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = new Fragment();
            switch (item.getItemId()) {
                case R.id.opcion_registrar_redencion:
                    fragment = new RendicionGastosFragment();
                    break;
                case R.id.opcion_listar_redencion:
                    fragment = new RendicionListadoFragment();
                    break;
            }
            FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_redencion, fragment);
            fragmentTransaction.commit();
            return true;
        });

        if (DatosSesion.sesion.getRol_id() != 1) {
            bottomNavigation.getMenu().findItem(R.id.opcion_registrar_redencion).setVisible(false);
            bottomNavigation.setSelectedItemId(R.id.opcion_listar_redencion);
        } else {
            bottomNavigation.setSelectedItemId(R.id.opcion_registrar_redencion);
        }

        return view;
    }
}