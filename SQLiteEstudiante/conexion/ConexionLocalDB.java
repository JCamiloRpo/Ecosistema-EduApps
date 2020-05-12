package conexion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;

import entidades.Actividad;
import entidades.Estado;
import entidades.Estudiante;
import entidades.Estudiante_Actividad;
import entidades.Recurso;


public class ConexionLocalDB {
    private ConexionSQLiteHelper conn; //conexion a BD.

    public ConexionLocalDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        conn=new ConexionSQLiteHelper(context,name,factory,version);
    }

    /**
     * Metodos sobrecargados utilizados para la insercion de un nuevo objeto a la BD.
     * Reciben el objeto/registro que se desea Insertar.
     * @return resultado de la insercion
     */
    //Create
    public long Create(Estado estado ){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.insert("estados", null, estado.toContentValues());
    }
    public long Create(Actividad actividad){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.insert("actividades", null, actividad.toContentValues());
    }
    public long Create(Estudiante estudiante){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.insert("estudiantes", null, estudiante.toContentValues());
    }
    public long Create(Recurso recurso){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.insert("recursos", null, recurso.toContentValues());
    }
    public long Create(Estudiante_Actividad est_act){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.insert("estudiantes_actividades", null, est_act.toContentValues());
    }

    /**
     * Metodos para leer infromacion de la BD.
     * Reciben la llave primaria del registro que se desea buscar.
     * @return el objeto/registro encontrado o NULL en caso negativo de la consulta
     */
    //Read
    public Estado ReadEstado(int estadoID){
            SQLiteDatabase sqLiteDatabase = conn.getReadableDatabase();
            String query = "select * from estados WHERE estadoID=?";
            Cursor c= sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(estadoID)});
            if (c.moveToFirst()) return new Estado(estadoID,c.getString(c.getColumnIndex("descripcion")));
            return null;


    }
    public Actividad ReadActividad(int actividadID){
        SQLiteDatabase sqLiteDatabase = conn.getReadableDatabase();
        String query = "select * from actividades WHERE actividadID=?";
        Cursor c= sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(actividadID)});
        if (c.moveToFirst()) return new Actividad(actividadID,c.getInt(c.getColumnIndex("sesion_ID")),c.getString(c.getColumnIndex("descripcion")),c.getInt(c.getColumnIndex("tiempo")));
        return null;
    }
    public Estudiante ReadEstudiante(int estudianteID){
        SQLiteDatabase sqLiteDatabase = conn.getReadableDatabase();
        String query = "select * from estudiantes WHERE estudianteID=?";
        Cursor c= sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(estudianteID)});
        if (c.moveToFirst()) return new Estudiante(estudianteID,c.getString(c.getColumnIndex("identificacion")),c.getString(c.getColumnIndex("tipoIdentificacion")));
        return null;
    }

    public Recurso ReadRecurso(int recursoID){
        SQLiteDatabase sqLiteDatabase = conn.getReadableDatabase();
        String query = "select * from recursos WHERE estudiante_ID=?";
        Cursor c= sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(recursoID)});
        if (c.moveToFirst()) return new Recurso(recursoID,c.getInt(c.getColumnIndex("actividad_ID")),c.getString(c.getColumnIndex("hipervinculo")));
        return null;
    }
    public Estudiante_Actividad ReadEstudiante_Actividad(int estudiante_ID,int actividad_ID){
        SQLiteDatabase sqLiteDatabase = conn.getReadableDatabase();
        String query = "select * from estudiantes_actividades WHERE estdiante_ID=? AND actividad_ID=?";
        Cursor c= sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(estudiante_ID),String.valueOf(actividad_ID)});
        if (c.moveToFirst()) return new Estudiante_Actividad(estudiante_ID,actividad_ID,c.getInt(c.getColumnIndex("estado_ID")),c.getString(c.getColumnIndex("observaciones")));
        return null;
    }

    /**
     * Metodos utilizados para actualizar la BD
     * Reciben la llave primaria de la tabla en cuestion
     * y un objeto con los parametros que se desean cambiar.
     * @return el resultado de la operacion en la BD
     */
    //Update
    public int UpdateEstado(Estado estado,int estadoID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.update("estados",estado.toContentValues(),"estadoID LIKE ?",new String[]{String.valueOf(estadoID)});
    }
    public int UpdateActividad(Actividad actividad,int actividadID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.update("actividades",actividad.toContentValues(),"actividadID LIKE ?",new String[]{String.valueOf(actividadID)});
    }
    public int UpdateEstudiante(Estudiante estudiante,int estudianteID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.update("estudiantes",estudiante.toContentValues(),"actividadID LIKE ?",new String[]{String.valueOf(estudianteID)});
    }
    public int UpdateRecurso(Recurso recurso,int recursoID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.update("recursos",recurso.toContentValues(),"recursoID LIKE ?",new String[]{String.valueOf(recursoID)});
    }
    public int UpdateEstudiante_Actividad(Estudiante_Actividad estudiante_Actividad,int estudiante_ID,int actividad_ID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.update("recursos",estudiante_Actividad.toContentValues(),"estdiante_ID LIKE ? AND actividad_ID LIKE ?",new String[]{String.valueOf(estudiante_ID),String.valueOf(actividad_ID)});
    }

    /**
     * Metodos Utilizados para eliminar informacion de la BD
     * Reciben la llave primaria del objeto que se desea eliminar
     * @return el resultado de la operacion en la BD
     */
    //Delete
    public int DeleteEstado(int estadoID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.delete("estados","estodoID LIKE ?",new String[]{String.valueOf(estadoID)});
    }
    public int DeleteActividad(int actividadID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.delete("actividades","actividadID LIKE ?",new String[]{String.valueOf(actividadID)});
    }
    public int DeleteEstudiante(int estudianteID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.delete("estudiantes","estudianteID LIKE ?",new String[]{String.valueOf(estudianteID)});
    }
    public int DeleteRecurso(int recursoID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.delete("recursos","recursoID LIKE ?",new String[]{String.valueOf(recursoID)});
    }
    public int DeleteEstudiante_Actividad(int estudiante_ID,int actividad_ID){
        SQLiteDatabase sqLiteDatabase = conn.getWritableDatabase();
        return sqLiteDatabase.delete("estudiantes_actividades","estdiante_ID LIKE ? AND actividad_ID LIKE ?",new String[]{String.valueOf(estudiante_ID),String.valueOf(actividad_ID)});
    }

}
