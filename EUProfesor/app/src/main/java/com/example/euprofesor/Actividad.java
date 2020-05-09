package com.example.euprofesor;

import android.widget.ImageButton;

public class Actividad {

    private String idActividad, descripcion, tiempo, recurso;
    private ImageButton btnExpand;

    public Actividad(String idActividad, String descripcion, String tiempo, String recurso, ImageButton btnExpand) {
        this.idActividad = idActividad;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.recurso = recurso;
        this.btnExpand = btnExpand;
    }

    public String getIdActividad() {
        return idActividad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTiempo() {
        return tiempo;
    }

    public String getRecurso() {
        return recurso;
    }

    public ImageButton getBtnExpand() {
        return btnExpand;
    }
}
