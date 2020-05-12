package entidades;

import android.content.ContentValues;

import java.io.Serializable;

public class Estudiante_Actividad implements Serializable {
    private Integer estudiante_ID;
    private Integer actividad_ID;
    private Integer estado_ID;
    private String observaciones;

    public Estudiante_Actividad(Integer estudiante_ID, Integer actividad_ID, Integer estado_ID, String observaciones) {
        this.estudiante_ID = estudiante_ID;
        this.actividad_ID = actividad_ID;
        this.estado_ID = estado_ID;
        this.observaciones = observaciones;
    }

    public Integer getEstudiante_ID() {
        return estudiante_ID;
    }

    public void setEstudiante_ID(Integer estudiante_ID) {
        this.estudiante_ID = estudiante_ID;
    }

    public Integer getActividad_ID() {
        return actividad_ID;
    }

    public void setActividad_ID(Integer actividad_ID) {
        this.actividad_ID = actividad_ID;
    }

    public Integer getEstado_ID() {
        return estado_ID;
    }

    public void setEstado_ID(Integer estado_ID) {
        this.estado_ID = estado_ID;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    /**
     * Este metodo auxiliar es utilizado para realizar
     * una interpretacion del objeto como un ContentValues
     * @return representacion del objeto en ContentValue
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("actividad_ID", estudiante_ID);
        values.put("actividad_ID", actividad_ID);
        values.put("estado_ID",estado_ID);
        values.put("observaciones", observaciones);
        return values;
    }
}
