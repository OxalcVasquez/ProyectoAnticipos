package com.usat.desarrollo.moviles.appanticipos.data.remote.response;

import android.content.Context;

import com.usat.desarrollo.moviles.appanticipos.domain.modelo.Anticipo;

import java.util.ArrayList;

public class AnticipoListadoResponse {
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
