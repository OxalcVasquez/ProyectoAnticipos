package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.HistorialAnticipo;

import java.util.List;

public class UltimaInstanciaResponse {

    private HistorialAnticipo data;
    private String message;
    private Boolean status;

    public HistorialAnticipo getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }
}
