package com.usat.desarrollo.moviles.appanticipos.modelo;

public class Sede {
    private int idSede;
    private String nombreSede;

    public Sede(int idSede, String nombreSede) {
        this.idSede = idSede;
        this.nombreSede = nombreSede;
    }

    public Sede() {
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }
}
