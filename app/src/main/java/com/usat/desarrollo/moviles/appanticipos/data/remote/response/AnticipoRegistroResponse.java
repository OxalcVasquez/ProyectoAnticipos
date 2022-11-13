package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Usuario;

import java.util.List;

public class AnticipoRegistroResponse {
    private Anticipo data;
    private String message;
    private Boolean status;

    public Anticipo getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }
}
