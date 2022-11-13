package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

public class Tarifa {
    private int id;
    private float monto_maximo;
    private String rubro;
    private int rubro_id;
    private String sede;
    private int sede_id;
    private String se_calcula_por_dia;

    public String getSe_calcula_por_dia() {
        return se_calcula_por_dia;
    }

    public int getId() {
        return id;
    }

    public float getMonto_maximo() {
        return monto_maximo;
    }

    public String getRubro() {
        return rubro;
    }

    public int getRubro_id() {
        return rubro_id;
    }

    public String getSede() {
        return sede;
    }

    public int getSede_id() {
        return sede_id;
    }
}
