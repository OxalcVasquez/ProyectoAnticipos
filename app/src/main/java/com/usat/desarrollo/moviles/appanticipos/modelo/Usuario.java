package com.usat.desarrollo.moviles.appanticipos.modelo;

public class Usuario {
    private int usuarioId, rolId;
    private String email, nombres, apellidos;

    public Usuario(int usuarioId, int rolId, String email, String nombres, String apellidos) {
        this.usuarioId = usuarioId;
        this.rolId = rolId;
        this.email = email;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public Usuario() {
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}
