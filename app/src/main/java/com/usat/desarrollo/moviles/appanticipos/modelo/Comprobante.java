package com.usat.desarrollo.moviles.appanticipos.modelo;

import java.util.ArrayList;
import java.util.Date;

public class Comprobante {
    private int idCom, tipoComprobanteId, rubroId, numOperacion, informeGastoId;
    private String serie, correlativo, ruc, descripcion, foto;
    private Double montoTotal;
    private Date fechaEmision;

    public static ArrayList<Comprobante> comprobanteListado = new ArrayList<>();

    public Comprobante(int idCom, int tipoComprobanteId, int rubroId, int numOperacion, int informeGastoId, String serie, String correlativo, String ruc, String descripcion, String foto, Double montoTotal, Date fechaEmision) {
        this.idCom = idCom;
        this.tipoComprobanteId = tipoComprobanteId;
        this.rubroId = rubroId;
        this.numOperacion = numOperacion;
        this.informeGastoId = informeGastoId;
        this.serie = serie;
        this.correlativo = correlativo;
        this.ruc = ruc;
        this.descripcion = descripcion;
        this.foto = foto;
        this.montoTotal = montoTotal;
        this.fechaEmision = fechaEmision;
    }

    public Comprobante() {
    }

    public int getIdCom() {
        return idCom;
    }

    public void setIdCom(int idCom) {
        this.idCom = idCom;
    }

    public int getTipoComprobanteId() {
        return tipoComprobanteId;
    }

    public void setTipoComprobanteId(int tipoComprobanteId) {
        this.tipoComprobanteId = tipoComprobanteId;
    }

    public int getRubroId() {
        return rubroId;
    }

    public void setRubroId(int rubroId) {
        this.rubroId = rubroId;
    }

    public int getNumOperacion() {
        return numOperacion;
    }

    public void setNumOperacion(int numOperacion) {
        this.numOperacion = numOperacion;
    }

    public int getInformeGastoId() {
        return informeGastoId;
    }

    public void setInformeGastoId(int informeGastoId) {
        this.informeGastoId = informeGastoId;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(String correlativo) {
        this.correlativo = correlativo;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void cargarDatosComprobante() {

    }
}
