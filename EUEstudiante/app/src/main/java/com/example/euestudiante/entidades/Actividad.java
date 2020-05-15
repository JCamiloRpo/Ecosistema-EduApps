package com.example.euestudiante.entidades;

import android.content.ContentValues;

public class Actividad {
    private int actividadID;
    private int sesion_ID;
    private String descripcion;
    private int tiempo;

    public Actividad(int actividadID, int sesion_ID, String descripcion, int tiempo) {
        this.actividadID = actividadID;
        this.sesion_ID = sesion_ID;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
    }

    public int getActividadID() {
        return actividadID;
    }

    public void setActividadID(int actividadID) {
        this.actividadID = actividadID;
    }

    public int getSesion_ID() {
        return sesion_ID;
    }

    public void setSesion_ID(int sesion_ID) {
        this.sesion_ID = sesion_ID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * Este metodo auxiliar es utilizado para realizar
     * una interpretacion del objeto como un ContentValues
     * @return representacion del objeto en ContentValue
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("ID", actividadID);
        values.put("Sesion_ID",sesion_ID);
        values.put("Descripcion", descripcion);
        values.put("Tiempo", tiempo);
        return values;
    }
}
