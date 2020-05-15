package com.example.euestudiante;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.euestudiante.entidades.*;
import com.example.euestudiante.entidades.Actividad;

public class ConexionLocalDB {

    private ConexionSQLiteHelper conn; //conexion a BD.

    public ConexionLocalDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        conn = new ConexionSQLiteHelper(context,name,factory,version);
    }

    /**
     * Metodos sobrecargados utilizados para la insercion de un nuevo objeto a la BD.
     * Reciben el objeto/registro que se desea Insertar.
     * @return resultado de la insercion
     */
    //Insert
    public long Insert(Estado estado ){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.insert("Estados", null, estado.toContentValues());
    }

    public long Insert(Actividad actividad){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.insert("Actividades", null, actividad.toContentValues());
    }

    public long Insert(Estudiante estudiante){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.insert("Estudiantes", null, estudiante.toContentValues());
    }

    public long Insert(Recurso recurso){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.insert("Recursos", null, recurso.toContentValues());
    }

    public long Insert(Estudiante_Actividad est_act){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.insert("Estudiantes_Actividades", null, est_act.toContentValues());
    }

    /**
     * Metodo para leer infromacion de la BD.
     * @return un arreglo con la misma estructura que el API REST
     */
    public String[][] Read(String table){
        String[][] result;
        SQLiteDatabase sqLiteDatabase = conn.getReadableDatabase();
        String query = "SELECT * FROM "+table;
        Cursor c= sqLiteDatabase.rawQuery(query, null);
        if (c.moveToFirst()) {
            result = new String[c.getCount()][c.getColumnCount()];
            int i=0;
            do {
                for(int j=0; j<c.getColumnCount(); j++)
                    result[i][j] = c.getString(j);
                i++;
            } while (c.moveToNext());
        }
        else
            result=new String[0][0];

        return result;
    }

    /**
     * Metodo para leer infromacion de la BD.
     * Reciben la tabla y la condicion a buscar
     * @return un arreglo con la misma estructura que el API REST
     */
    public String[][] Read(String table, String where){
        String[][] result;
        SQLiteDatabase sqLiteDatabase = conn.getReadableDatabase();
        String query = "SELECT * FROM "+table+" WHERE "+where;
        Cursor c= sqLiteDatabase.rawQuery(query, null);
        if (c.moveToFirst()) {
            result = new String[c.getCount()][c.getColumnCount()];
            int i=0;
            do {
                for(int j=0; j<c.getColumnCount(); j++)
                    result[i][j] = c.getString(j);
                i++;
            } while (c.moveToNext());
        }
        else
            result=new String[0][0];

        return result;
    }

    /**
     * Metodo para leer infromacion de la BD.
     * Reciben la tabla, las columnas y la condicion a buscar
     * @return un arreglo con la misma estructura que el API REST
     */
    public String[][] Read(String table, String columns, String where){
        String[][] result;
        SQLiteDatabase sqLiteDatabase = conn.getReadableDatabase();
        String query = "SELECT "+columns+" FROM "+table+" WHERE "+where;
        Cursor c= sqLiteDatabase.rawQuery(query, null);
        if (c.moveToFirst()) {
            result = new String[c.getCount()][c.getColumnCount()];
            int i=0;
            do {
                for(int j=0; j<c.getColumnCount(); j++)
                    result[i][j] = c.getString(j);
                i++;
            } while (c.moveToNext());
        }
        else
            result=new String[0][0];

        return result;
    }

    /**
     * Metodos utilizados para actualizar la BD
     * Reciben la llave primaria de la tabla en cuestion
     * y un objeto con los parametros que se desean cambiar.
     * @return el resultado de la operacion en la BD
     */
    //Update
    public int Update(Estado estado,int estadoID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
        return sqLiteDatabase.update("Estados",estado.toContentValues(),"ID = ?",new String[]{String.valueOf(estadoID)});
    }

    public int Update(Actividad actividad, int actividadID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
        return sqLiteDatabase.update("Actividades",actividad.toContentValues(),"ID = ?",new String[]{String.valueOf(actividadID)});
    }

    public int Update(Estudiante estudiante,int estudianteID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
        return sqLiteDatabase.update("Estudiantes",estudiante.toContentValues(),"ID = ?",new String[]{String.valueOf(estudianteID)});
    }

    public int Update(Recurso recurso,int recursoID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
        return sqLiteDatabase.update("Recursos",recurso.toContentValues(),"ID = ?",new String[]{String.valueOf(recursoID)});
    }

    public int Update(Estudiante_Actividad estudiante_Actividad,int estudiante_ID,int actividad_ID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
        return sqLiteDatabase.update("Estudiantes_Actividades",estudiante_Actividad.toContentValues(),"Estudiante_ID = ? AND Actividad_ID = ?",new String[]{String.valueOf(estudiante_ID),String.valueOf(actividad_ID)});
    }
}
