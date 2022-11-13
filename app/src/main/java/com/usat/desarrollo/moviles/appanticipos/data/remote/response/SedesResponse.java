package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.MotivoAnticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Sede;

import java.util.List;

public class SedesResponse {

    private List<Sede> data;
    private String message;
    private Boolean status;

    public List<Sede> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

}
