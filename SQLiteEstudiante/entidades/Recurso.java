package entidades;

import android.content.ContentValues;

public class Recurso {
    private int recursoID;
    private String hipervinculo;
    private int actividad_ID;

    public Recurso(int recursoID, int actividad_ID, String hipervinculo) {
        this.recursoID = recursoID;
        this.actividad_ID = actividad_ID;
        this.hipervinculo = hipervinculo;
    }

    public int getRecursoID() {
        return recursoID;
    }

    public void setRecursoID(int recursoID) {
        this.recursoID = recursoID;
    }

    public String getHipervinculo() {
        return hipervinculo;
    }

    public void setHipervinculo(String hipervinculo) {
        this.hipervinculo = hipervinculo;
    }

    public int getActividad_ID() {
        return actividad_ID;
    }

    public void setActividad_ID(int actividad_ID) {
        this.actividad_ID = actividad_ID;
    }
    /**
     * Este metodo auxiliar es utilizado para realizar
     * una interpretacion del objeto como un ContentValues
     * @return representacion del objeto en ContentValue
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("recursoID", recursoID);
        values.put("actividad_ID", actividad_ID);
        values.put("hipervinculo", hipervinculo);
        return values;
    }
}
