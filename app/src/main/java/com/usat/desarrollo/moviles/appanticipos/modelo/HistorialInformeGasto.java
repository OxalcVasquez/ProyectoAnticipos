package com.usat.desarrollo.moviles.appanticipos.modelo;

import java.util.Date;

public class HistorialInformeGasto {
    private int id, informeGastoId, usuarioId;
    private String estado;
    private Date fecha;

    public HistorialInformeGasto(int id, int informeGastoId, int usuarioId, String estado, Date fecha) {
        this.id = id;
        this.informeGastoId = informeGastoId;
        this.usuarioId = usuarioId;
        this.estado = estado;
        this.fecha = fecha;
    }

    public HistorialInformeGasto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInformeGastoId() {
        return informeGastoId;
    }

    public void setInformeGastoId(int informeGastoId) {
        this.informeGastoId = informeGastoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
