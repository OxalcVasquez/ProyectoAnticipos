package com.usat.desarrollo.moviles.appanticipos;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                case R.id.opcion_listar_anticipos:
                    fragment = new RendicionListadoFragment();
                    break;
            }
            FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_redencion, fragment);
            fragmentTransaction.commit();
            return true;
        });
        bottomNavigation.setSelectedItemId(R.id.opcion_registrar_redencion);
        return view;
    }
}