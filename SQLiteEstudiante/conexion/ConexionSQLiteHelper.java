package conexion;

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

    final String Crear_Tabla_estados="CREATE TABLE estados(estadoID INTEGER PRIMARY KEY, descripcion TEXT)";

    final String Crear_Tabla_recursos="CREATE TABLE recursos(recursoID INTEGER PRIMARY KEY," +
            "actividad_ID INTEGER,hipervinculo TEXT,FOREIGN KEY (actividad_ID) REFERENCES actividades(actividadID) ON DELETE CASCADE ON UPDATE CASCADE)";

    final String Crear_Tabla_estudiantes="CREATE TABLE estudiantes(estudianteID INTEGER PRIMARY KEY," +
            "identificacion TEXT,tipoIdentificacion TEXT)";

    final String Crear_Tabla_actividades="CREATE TABLE actividades(actividadID INTEGER PRIMARY KEY, " +
            "sesion_ID INTEGER, descripcion TEXT, tiempo INTEGER)";

    final String Crear_Tabla_estudiante_actividades="CREATE TABLE estudiantes_actividades(estudiante_ID INTEGER NOT NULL, " +
            "actividad_ID INTEGER NOT NULL, estado_ID INTEGER, observaciones TEXT,FOREIGN KEY (estudiante_ID) REFERENCES estudiantes(estudianteID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (actividad_ID) REFERENCES actividades(actividadID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (estado_ID) REFERENCES estados(estadoID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "PRIMARY KEY ( estudiante_ID, actividad_ID) )";



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
        //o
        db.setForeignKeyConstraintsEnabled(true);
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
        //o
        db.setForeignKeyConstraintsEnabled(true);

    }


}
