package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

import java.util.List;

public class Sede {
    private int id;
    private String nombre;

    public static List<Sede> listaSedes;

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
