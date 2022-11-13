package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Sede;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Tarifa;

import java.util.List;

public class TarifaResponse {
    private List<Tarifa> data;
    private String message;
    private Boolean status;

    public List<Tarifa> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

}
