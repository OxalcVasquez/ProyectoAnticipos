package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class MotivoAnticipo{
    private int id;
    private String descripcion;

    public static List<MotivoAnticipo> listaMotivos;

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return id + ". "+ descripcion;
    }
}
