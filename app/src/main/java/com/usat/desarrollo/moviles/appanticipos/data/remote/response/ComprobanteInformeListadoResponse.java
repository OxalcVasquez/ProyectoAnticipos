package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Comprobante;

import java.util.List;

public class ComprobanteInformeListadoResponse {
    private List<Comprobante> data;
    private String message;
    private Boolean status;

    public List<Comprobante> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }
}
