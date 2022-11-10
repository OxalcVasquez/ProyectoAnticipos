package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

import java.util.ArrayList;
import java.util.Date;

public class InformeGasto {
    private int id, anticipoId, usuarioId;
    private String numInf, estadoAnt;
    private Date fecha;
    private Double totalRendir, totalRendido;

    public static ArrayList<InformeGasto> listaInformeGasto = new ArrayList<>();

    public InformeGasto(int id, int anticipoId, int usuarioId, String numInf, String estadoAnt, Date fecha, Double totalRendir, Double totalRendido) {
        this.id = id;
        this.anticipoId = anticipoId;
        this.usuarioId = usuarioId;
        this.numInf = numInf;
        this.estadoAnt = estadoAnt;
        this.fecha = fecha;
        this.totalRendir = totalRendir;
        this.totalRendido = totalRendido;
    }

    public InformeGasto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnticipoId() {
        return anticipoId;
    }

    public void setAnticipoId(int anticipoId) {
        this.anticipoId = anticipoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNumInf() {
        return numInf;
    }

    public void setNumInf(String numInf) {
        this.numInf = numInf;
    }

    public String getEstadoAnt() {
        return estadoAnt;
    }

    public void setEstadoAnt(String estadoAnt) {
        this.estadoAnt = estadoAnt;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getTotalRendir() {
        return totalRendir;
    }

    public void setTotalRendir(Double totalRendir) {
        this.totalRendir = totalRendir;
    }

    public Double getTotalRendido() {
        return totalRendido;
    }

    public void setTotalRendido(Double totalRendido) {
        this.totalRendido = totalRendido;
    }

    public void cargarDatosRendicion() {

    }
}
