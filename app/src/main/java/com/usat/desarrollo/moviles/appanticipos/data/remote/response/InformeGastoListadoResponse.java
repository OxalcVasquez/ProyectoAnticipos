package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.InformeGasto;

import java.util.List;

public class InformeGastoListadoResponse {

    private List<InformeGasto> data;
    private String message;
    private Boolean status;

    public List<InformeGasto> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }
}
