package com.example.euestudiante;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    /**
     * Queries para la creacion de la Tablas, donde se definen
     * las columnas de cada tabla con el tipo correspondoiente de datos,
     * la llave primaria y las llaves foraneas
     * para estas ultimas en caso de eliminacion o actualizacion
     * la respuesta sera hacer esto mismo en cascada.
     */
    String Crear_Tabla_estados="CREATE TABLE Estados(" +
            "ID INTEGER PRIMARY KEY, " +
            "Descripcion TEXT)";

    String Crear_Tabla_estudiantes="CREATE TABLE Estudiantes(" +
            "ID INTEGER PRIMARY KEY," +
            "Identificacion TEXT," +
            "TipoIdentificacion TEXT)";

    String Crear_Tabla_actividades="CREATE TABLE Actividades(" +
            "ID INTEGER PRIMARY KEY, " +
            "Sesion_ID INTEGER, " +
            "Descripcion TEXT, " +
            "Tiempo INTEGER)";

    String Crear_Tabla_recursos="CREATE TABLE Recursos(" +
            "ID INTEGER PRIMARY KEY," +
            "Actividad_ID INTEGER," +
            "Hipervinculo TEXT," +
            "FOREIGN KEY (Actividad_ID) REFERENCES Actividades(ID) " +
            "ON DELETE CASCADE ON UPDATE CASCADE)";

    String Crear_Tabla_estudiante_actividades="CREATE TABLE Estudiantes_Actividades(" +
            "Estudiante_ID INTEGER NOT NULL, " +
            "Actividad_ID INTEGER NOT NULL, " +
            "Estado_ID INTEGER, " +
            "Observaciones TEXT," +
            "FOREIGN KEY (Estudiante_ID) REFERENCES Estudiantes(ID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (Actividad_ID) REFERENCES Actividades(ID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (Estado_ID) REFERENCES Estados(ID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "PRIMARY KEY (Estudiante_ID, Actividad_ID) )";



    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Crear_Tabla_estados);
        db.execSQL(Crear_Tabla_estudiantes);
        db.execSQL(Crear_Tabla_actividades);
        db.execSQL(Crear_Tabla_recursos);
        db.execSQL(Crear_Tabla_estudiante_actividades);
        //Activar las foreign Keys
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS estudiantes_actividades");
        db.execSQL("DROP TABLE IF EXISTS estados");
        db.execSQL("DROP TABLE IF EXISTS recursos");
        db.execSQL("DROP TABLE IF EXISTS estudiantes");
        db.execSQL("DROP TABLE IF EXISTS actividades");

        db.execSQL(Crear_Tabla_estados);
        db.execSQL(Crear_Tabla_estudiantes);
        db.execSQL(Crear_Tabla_actividades);
        db.execSQL(Crear_Tabla_recursos);
        db.execSQL(Crear_Tabla_estudiante_actividades);
        //Activar las foreign Keys
        db.execSQL("PRAGMA foreign_keys=ON");

    }


}
