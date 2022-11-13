package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

public class Sede {
    private int id;
    private String nombre;

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return id + ". "+ nombre;
    }

}
