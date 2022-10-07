package com.usat.desarrollo.moviles.appanticipos.modelo;

import java.util.Date;

public class Anticipo {
    private int idAnt, motivoAnticipoId, sedeId, usuarioId;
    private Date fechaInicio, fechaFin;
    private String descripcionAnt, estadoAnt;

    public Anticipo(int idAnt, int motivoAnticipoId, int sedeId, int usuarioId, Date fechaInicio, Date fechaFin, String descripcionAnt, String estadoAnt) {
        this.idAnt = idAnt;
        this.motivoAnticipoId = motivoAnticipoId;
        this.sedeId = sedeId;
        this.usuarioId = usuarioId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcionAnt = descripcionAnt;
        this.estadoAnt = estadoAnt;
    }

    public Anticipo() {
    }

    public int getIdAnt() {
        return idAnt;
    }

    public void setIdAnt(int idAnt) {
        this.idAnt = idAnt;
    }

    public int getMotivoAnticipoId() {
        return motivoAnticipoId;
    }

    public void setMotivoAnticipoId(int motivoAnticipoId) {
        this.motivoAnticipoId = motivoAnticipoId;
    }

    public int getSedeId() {
        return sedeId;
    }

    public void setSedeId(int sedeId) {
        this.sedeId = sedeId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcionAnt() {
        return descripcionAnt;
    }

    public void setDescripcionAnt(String descripcionAnt) {
        this.descripcionAnt = descripcionAnt;
    }

    public String getEstadoAnt() {
        return estadoAnt;
    }

    public void setEstadoAnt(String estadoAnt) {
        this.estadoAnt = estadoAnt;
    }
}
