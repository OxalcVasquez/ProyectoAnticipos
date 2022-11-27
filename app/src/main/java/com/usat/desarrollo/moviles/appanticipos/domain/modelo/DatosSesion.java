package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

public class DatosSesion {

    public static Usuario sesion;
    public static void clear() {
        sesion.setEmail("");
        sesion.setNombres("");
        sesion.setToken("");
        sesion.setId(-1);
        sesion.setImg("");
    }
}
