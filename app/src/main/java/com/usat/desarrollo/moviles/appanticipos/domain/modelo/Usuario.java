package com.usat.desarrollo.moviles.appanticipos.domain.modelo;

public class Usuario {
    private int usuarioId, rolId;
    private String email, nombres, apellidos, password, estado_usuario, token, estado_token;

    public Usuario(int usuarioId, int rolId, String email, String nombres, String apellidos, String password, String estado_usuario, String token, String estado_token) {
        this.usuarioId = usuarioId;
        this.rolId = rolId;
        this.email = email;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.password = password;
        this.estado_usuario = estado_usuario;
        this.token = token;
        this.estado_token = estado_token;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEstado_usuario() {
        return estado_usuario;
    }

    public void setEstado_usuario(String estado_usuario) {
        this.estado_usuario = estado_usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEstado_token() {
        return estado_token;
    }

    public void setEstado_token(String estado_token) {
        this.estado_token = estado_token;
    }
}
