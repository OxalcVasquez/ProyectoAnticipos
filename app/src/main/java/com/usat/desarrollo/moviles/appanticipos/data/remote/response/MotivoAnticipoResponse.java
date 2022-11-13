package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.MotivoAnticipo;

import java.util.List;

public class MotivoAnticipoResponse {

    private List<MotivoAnticipo> data;
    private String message;
    private Boolean status;

    public List<MotivoAnticipo> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

}
