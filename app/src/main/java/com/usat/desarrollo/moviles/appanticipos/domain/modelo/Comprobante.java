package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Comprobante implements Serializable {
    @SerializedName("comprobante")
    private String idCom;
    private int tipoComprobanteId;
    private int rubroId;
    private String numOperacion;
    private int informeGastoId;
    private String serie;
    private String correlativo;
    private String ruc;
    private String descripcion;
    private String foto;
    private String rubro;
    private String tipoComprobante;
    @SerializedName("monto_total")
    private Double montoTotal;
    @SerializedName("fecha_emision")
    private String fechaEmision;

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public static ArrayList<Comprobante> comprobanteListado = new ArrayList<>();


    public String getIdCom() {
        return idCom;
    }

    public void setIdCom(String idCom) {
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

    public String getNumOperacion() {
        return numOperacion;
    }

    public void setNumOperacion(String numOperacion) {
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

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public JSONObject getJSONComprobantes(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("serie", this.getSerie());
            jsonObject.put("correlativo", this.getCorrelativo());
            jsonObject.put("fecha_emision", this.getFechaEmision());
            jsonObject.put("monto_total", this.getMontoTotal());
            jsonObject.put("ruc", this.getRuc());
            jsonObject.put("descripcion", this.getDescripcion());
            jsonObject.put("tipo_comprobante_id", this.getTipoComprobanteId());
            jsonObject.put("rubro_id", this.getRubroId());
            jsonObject.put("foto", this.getFoto());
            jsonObject.put("num_operacion", this.getNumOperacion());
        } catch (Exception e){
            e.printStackTrace();
        }
        return  jsonObject;
    }

    @Override
    public String toString() {
        return "Comprobante{" +
                "idCom=" + idCom +
                ", tipoComprobanteId=" + tipoComprobanteId +
                ", rubroId=" + rubroId +
                ", numOperacion=" + numOperacion +
                ", informeGastoId=" + informeGastoId +
                ", serie='" + serie + '\'' +
                ", correlativo='" + correlativo + '\'' +
                ", ruc='" + ruc + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", foto='" + foto + '\'' +
                ", rubro='" + rubro + '\'' +
                ", tipoComprobante='" + tipoComprobante + '\'' +
                ", montoTotal=" + montoTotal +
                ", fechaEmision='" + fechaEmision + '\'' +
                '}';
    }
}
