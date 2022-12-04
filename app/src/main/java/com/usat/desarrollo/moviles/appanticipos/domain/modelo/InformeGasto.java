package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InformeGasto {

    private int id;
    @SerializedName("anticipo_id")
    private int anticipoId;
    @SerializedName("num_informe")
    private String numInforme;
    private String anticipo;
    private String estado;
    @SerializedName("fecha_hora")
    private String fechaRegistro;
    @SerializedName("fecha_inicio")
    private String fechaInicion;
    @SerializedName("fecha_fin")
    private String fechaFin;
    @SerializedName("total_rendido")
    private double totalRendido;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumInforme() {
        return numInforme;
    }

    public void setNumInforme(String numInforme) {
        this.numInforme = numInforme;
    }

    public String getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(String anticipo) {
        this.anticipo = anticipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public double getTotalRendido() {
        return totalRendido;
    }

    public void setTotalRendido(double totalRendido) {
        this.totalRendido = totalRendido;
    }

    public String getFechaInicion() {
        return fechaInicion;
    }

    public void setFechaInicion(String fechaInicion) {
        this.fechaInicion = fechaInicion;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getAnticipoId() {
        return anticipoId;
    }

    public void setAnticipoId(int anticipoId) {
        this.anticipoId = anticipoId;
    }
}
