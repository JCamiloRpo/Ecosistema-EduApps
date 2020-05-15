package com.example.euestudiante.entidades;

import android.content.ContentValues;

import java.io.Serializable;

public class Estado implements Serializable {
    private Integer estadoID;
    private String descripcion;

    public Estado(Integer estadoID, String descripcion) {
        this.estadoID = estadoID;
        this.descripcion = descripcion;
    }

    public Integer getEstadoID() {
        return estadoID;
    }

    public void setEstadoID(Integer estadoID) {
        this.estadoID = estadoID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Este metodo auxiliar es utilizado para realizar
     * una interpretacion del objeto como un ContentValues
     * @return representacion del objeto en ContentValue
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("ID", estadoID);
        values.put("Descripcion", descripcion);
        return values;
    }
}
