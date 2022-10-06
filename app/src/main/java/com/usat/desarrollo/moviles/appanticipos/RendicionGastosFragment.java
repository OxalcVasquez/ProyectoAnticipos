package com.usat.desarrollo.moviles.appanticipos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class RendicionGastosFragment extends Fragment {

    TextView txtDocente, txtPasajes, txtPasajesDe, txtAlimentacion, txtAlimentacionDe, txtHotel, txtHotelDe, txtMovilidad, txtMovilidadDe, txtDevolucion, txtRestante;
    AutoCompleteTextView actvAnticipos;
    Button btnAgregarComprobante, btnRegistrarRedencion;
    RecyclerView recyclerComprobante;
    int pos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rendicion_gastos, container, false);

        this.getActivity().setTitle(getString(R.string.title_registro_rendicion_gastos));

        txtDocente = view.findViewById(R.id.txt_docente_rendicion);
        actvAnticipos = view.findViewById(R.id.actv_anticipos_rendicion);
        txtPasajes = view.findViewById(R.id.txt_pasajes_rendicion);
        txtPasajesDe = view.findViewById(R.id.txt_pasajes_de_rendicion);
        txtAlimentacion = view.findViewById(R.id.txt_alimentacion_rendicion);
        txtAlimentacionDe = view.findViewById(R.id.txt_alimentacion_de_rendicion);
        txtHotel = view.findViewById(R.id.txt_hotel_rendicion);
        txtHotelDe = view.findViewById(R.id.txt_hotel_de_rendicion);
        txtMovilidad = view.findViewById(R.id.txt_movilidad_rendicion);
        txtMovilidadDe = view.findViewById(R.id.txt_movilidad_de_rendicion);
        txtDevolucion = view.findViewById(R.id.txt_devolucion_rendicion);
        txtRestante = view.findViewById(R.id.txt_restante_rendicion);
        btnAgregarComprobante = view.findViewById(R.id.btn_comprobante_agregar_rendicion);
        btnRegistrarRedencion = view.findViewById(R.id.btn_registrar_rendicion);

        recyclerComprobante = view.findViewById(R.id.recycler_comprobantes_rendicion);
        recyclerComprobante.setHasFixedSize(true);
        recyclerComprobante.setLayoutManager(new LinearLayoutManager(this.getContext()));

        getPos();

        return view;
    }

    private void registrarRendicion() {
        btnRegistrarRedencion.setOnClickListener(view -> {
            if (validate()) {
                Toast.makeText(this.getActivity(), "Debes agregar un comprobante", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarComprobante() {
        btnAgregarComprobante.setOnClickListener(view -> {
        });
    }

    private void getPos() {
        actvAnticipos.setOnItemClickListener((adapterView, view, i, l) -> {
            pos = i;
        });
    }

    private boolean validate() {
        return actvAnticipos.getText().toString().equalsIgnoreCase("");
    }
}