package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Rubro;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.TipoComprobante;

import java.util.List;

public class RubrosResponse {

    private List<Rubro> data;
    private String message;
    private Boolean status;

    public List<Rubro> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }
}
