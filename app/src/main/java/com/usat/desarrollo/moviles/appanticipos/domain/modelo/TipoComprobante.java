package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

public class TipoComprobante {
    private int idCom;
    private String nombreCom;

    public TipoComprobante(int idCom, String nombreCom) {
        this.idCom = idCom;
        this.nombreCom = nombreCom;
    }

    public TipoComprobante() {
    }

    public int getIdCom() {
        return idCom;
    }

    public void setIdCom(int idCom) {
        this.idCom = idCom;
    }

    public String getNombreCom() {
        return nombreCom;
    }

    public void setNombreCom(String nombreCom) {
        this.nombreCom = nombreCom;
    }
}
