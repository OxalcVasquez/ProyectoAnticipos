package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

import java.util.Date;

public class HistorialAnticipo {
    private int id, usuarioId;
    private String estado;
    private Date fecha;

    public HistorialAnticipo(int id, int usuarioId, String estado, Date fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.estado = estado;
        this.fecha = fecha;
    }

    public HistorialAnticipo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
