package com.usat.desarrollo.moviles.appanticipos.modelo;

public class Tarifa {
    private int idRubro, idSede;
    private Double monto;

    public Tarifa(int idRubro, int idSede, Double monto) {
        this.idRubro = idRubro;
        this.idSede = idSede;
        this.monto = monto;
    }

    public Tarifa() {
    }

    public int getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(int idRubro) {
        this.idRubro = idRubro;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
