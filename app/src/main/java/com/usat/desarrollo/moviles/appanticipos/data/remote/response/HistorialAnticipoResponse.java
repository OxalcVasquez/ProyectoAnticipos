package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.HistorialAnticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.InformeGasto;

import java.util.List;

public class HistorialAnticipoResponse {

    private List<HistorialAnticipo> data;
    private String message;
    private Boolean status;

    public List<HistorialAnticipo> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }
}
