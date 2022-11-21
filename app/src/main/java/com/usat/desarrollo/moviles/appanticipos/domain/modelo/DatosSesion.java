package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

public class DatosSesion {
    public static String USUARIO_EMAIL;
    public static String USUARIO_NOMBRE;
    public static String USUARIO_IMG;
    public static String TOKEN;
    public static int USUARIO_ID;

    public static void clear() {
        USUARIO_EMAIL = "";
        USUARIO_NOMBRE = "";
        USUARIO_IMG = "";
        TOKEN = "";
        USUARIO_ID = -1;
    }
}
