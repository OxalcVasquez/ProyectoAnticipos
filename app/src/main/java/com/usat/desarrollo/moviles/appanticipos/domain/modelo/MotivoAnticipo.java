package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MotivoAnticipo{
    private int id;
    private String descripcion;

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
