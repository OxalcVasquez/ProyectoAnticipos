package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

import java.util.ArrayList;
import java.util.Date;

public class Anticipo {
    private int anticipo_id;
    private String descripcion;
    private String estado;
    private String fecha_fin;
    private String fecha_inicio;
    private String img;
    private float monto_total;
    public static ArrayList<Anticipo> listaAnticipos;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getAnticipo_id() {
        return anticipo_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public float getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(float monto_total) {
        this.monto_total = monto_total;
    }
}
