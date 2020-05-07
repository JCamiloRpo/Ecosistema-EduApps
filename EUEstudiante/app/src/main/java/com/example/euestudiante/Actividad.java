package com.example.euestudiante;

import android.widget.ImageButton;

public class Actividad {

    private String idActividad, descripcion, tiempo, recurso;
<<<<<<< HEAD
    private ImageButton btnDescarga, btnExpand, btnEstado;

    public Actividad(String idActividad, String descripcion, String tiempo, String recurso,
                     ImageButton btnDescarga, ImageButton btnExpand, ImageButton btnEstado) {
=======
    private ImageButton btnDescarga;

    public Actividad(String idActividad, String descripcion, String tiempo, String recurso, ImageButton btnDescarga) {
>>>>>>> f61d18877efb9fc2cbfa1ab33c1305109b1437c0
        this.idActividad = idActividad;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.recurso = recurso;
        this.btnDescarga = btnDescarga;
<<<<<<< HEAD
        this.btnExpand = btnExpand;
        this.btnEstado = btnEstado;
=======
>>>>>>> f61d18877efb9fc2cbfa1ab33c1305109b1437c0
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

<<<<<<< HEAD
    public ImageButton getBtnEstado() {
        return btnEstado;
    }
=======
>>>>>>> f61d18877efb9fc2cbfa1ab33c1305109b1437c0
}
