package com.example.euestudiante;

import android.widget.ImageButton;

public class Actividad {

    private String idActividad, descripcion, tiempo, recurso;
    private ImageButton btnDescarga;

    public Actividad(String idActividad, String descripcion, String tiempo, String recurso, ImageButton btnDescarga) {
        this.idActividad = idActividad;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.recurso = recurso;
        this.btnDescarga = btnDescarga;
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

    public ImageButton getBtnDescarga() {
        return btnDescarga;
    }

}
