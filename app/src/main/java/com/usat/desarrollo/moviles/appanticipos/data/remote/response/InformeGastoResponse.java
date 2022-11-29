package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.InformeGasto;

public class InformeGastoResponse {
    private InformeGasto data;
    private String message;
    private Boolean status;

    public InformeGasto getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }
}
