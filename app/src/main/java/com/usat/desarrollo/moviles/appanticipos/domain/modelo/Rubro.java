package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

public class Rubro {
    private int idRub;
    private String nombreRub, seCalculaPorDiaRub;

    public Rubro(int idRub, String nombreRub, String seCalculaPorDiaRub) {
        this.idRub = idRub;
        this.nombreRub = nombreRub;
        this.seCalculaPorDiaRub = seCalculaPorDiaRub;
    }

    public Rubro() {
    }

    public int getIdRub() {
        return idRub;
    }

    public void setIdRub(int idRub) {
        this.idRub = idRub;
    }

    public String getNombreRub() {
        return nombreRub;
    }

    public void setNombreRub(String nombreRub) {
        this.nombreRub = nombreRub;
    }

    public String getSeCalculaPorDiaRub() {
        return seCalculaPorDiaRub;
    }

    public void setSeCalculaPorDiaRub(String seCalculaPorDiaRub) {
        this.seCalculaPorDiaRub = seCalculaPorDiaRub;
    }
}
