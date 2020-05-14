package com.example.euestudiante.entidades;

import android.content.ContentValues;

public class Estudiante {
    private int estudianteID;
    private String identificacion;
    private String tipoIdentificacion;

    public Estudiante(int estudianteID, String identificacion, String tipoIdentificacion) {
        this.estudianteID = estudianteID;
        this.identificacion = identificacion;
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public int getEstudianteID() {
        return estudianteID;
    }

    public void setEstudianteID(int estudianteID) {
        this.estudianteID = estudianteID;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    /**
     * Este metodo auxiliar es utilizado para realizar
     * una interpretacion del objeto como un ContentValues
     * @return representacion del objeto en ContentValue
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("ID", estudianteID);
        values.put("Identificacion", identificacion);
        values.put("TipoIdentificacion", tipoIdentificacion);
        return values;
    }
}
