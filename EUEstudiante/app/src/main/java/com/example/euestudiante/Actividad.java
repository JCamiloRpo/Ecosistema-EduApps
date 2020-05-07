package com.example.euestudiante;

import android.widget.ImageButton;

public class Actividad {

    private String idActividad, descripcion, tiempo, recurso;
    private ImageButton btnDescarga, btnExpand, btnEstado;

    public Actividad(String idActividad, String descripcion, String tiempo, String recurso,
                     ImageButton btnDescarga, ImageButton btnExpand, ImageButton btnEstado) {
        this.idActividad = idActividad;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.recurso = recurso;
        this.btnDescarga = btnDescarga;
        this.btnExpand = btnExpand;
        this.btnEstado = btnEstado;
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

    public ImageButton getBtnEstado() {
        return btnEstado;
    }
}
