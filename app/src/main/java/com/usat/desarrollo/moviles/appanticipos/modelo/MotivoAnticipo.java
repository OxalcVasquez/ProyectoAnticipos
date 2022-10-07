package com.usat.desarrollo.moviles.appanticipos.modelo;

public class MotivoAnticipo {
    private int idAnt;
    private String descripcionAnt;

    public MotivoAnticipo(int idAnt, String descripcionAnt) {
        this.idAnt = idAnt;
        this.descripcionAnt = descripcionAnt;
    }

    public MotivoAnticipo() {
    }

    public int getIdAnt() {
        return idAnt;
    }

    public void setIdAnt(int idAnt) {
        this.idAnt = idAnt;
    }

    public String getDescripcionAnt() {
        return descripcionAnt;
    }

    public void setDescripcionAnt(String descripcionAnt) {
        this.descripcionAnt = descripcionAnt;
    }
}
