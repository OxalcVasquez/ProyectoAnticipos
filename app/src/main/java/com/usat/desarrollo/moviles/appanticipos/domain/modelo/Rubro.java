package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Rubro {
    private int id;
    private String nombre;
    @SerializedName("se_calcula_por_dia")
    private String seCalculaPorDia;
    public static List<Rubro> listaRubros;

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

    public String getSeCalculaPorDia() {
        return seCalculaPorDia;
    }

    public void setSeCalculaPorDia(String seCalculaPorDia) {
        this.seCalculaPorDia = seCalculaPorDia;
    }
}
