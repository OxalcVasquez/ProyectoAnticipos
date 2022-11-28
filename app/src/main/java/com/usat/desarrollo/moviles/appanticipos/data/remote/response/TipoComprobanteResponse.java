package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Sede;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.TipoComprobante;

import java.util.List;

public class TipoComprobanteResponse {

    private List<TipoComprobante> data;
    private String message;
    private Boolean status;

    public List<TipoComprobante> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }
}
