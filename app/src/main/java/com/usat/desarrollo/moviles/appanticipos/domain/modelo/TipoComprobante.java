package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

import java.util.List;

public class TipoComprobante {
    private int id;
    private String nombre;
    public static List<TipoComprobante> listaComprobamte;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



}
