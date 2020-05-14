package com.example.euestudiante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.euestudiante.entidades.Actividad;
import com.example.euestudiante.entidades.Estado;
import com.example.euestudiante.entidades.Estudiante;
import com.example.euestudiante.entidades.Estudiante_Actividad;
import com.example.euestudiante.entidades.Recurso;

public class MainActivity extends AppCompatActivity {

    public static ConexionFTPS ftps;
    public static ConexionApiRest apiRest;
    public static ConexionLocalDB localDB;
    public static String ID="0", idEstudiante ="0";
    public static Context context;
    public static boolean online;
    private EditText idSesion, numIdent;
    private Spinner tipoIdent;
    private Button btnSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        context = getApplicationContext();
        localDB = new ConexionLocalDB(this,"EduApps",null, 1);
        idSesion = findViewById(R.id.edit_sesion);
        tipoIdent = findViewById(R.id.spinner_tipoIdent);
        numIdent = findViewById(R.id.edit_numIdent);
        btnSesion = findViewById(R.id.btn_sesion);
    }

    /**
     * Validar los campos para poder inicial sesion
     * @param v
     */
    public void btnSesion(View v){
        //Utilizar API para conexion con base de datos
        if(idSesion.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Debe escribir un ID de sesion", Toast.LENGTH_SHORT).show();
        else if(tipoIdent.getSelectedItemPosition()==0)
            Toast.makeText(getApplicationContext(), "Debe selecionar un tipo de identificación", Toast.LENGTH_SHORT).show();
        else if(numIdent.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Debe escribir un numero de identificación", Toast.LENGTH_SHORT).show();
        else{
            ID = idSesion.getText().toString();
            new ConnectTask().execute();
        }
    }

    /**
     * Metodo que se ejecuta luego de probrar la conexion para saber si se estan en modo offline o online
     */
    private void iniciar(){
        Intent in = new Intent(this, ActividadActivity.class);
        boolean local = local();
        String[][] estados, estudiante, actividades, recursos, tmp;
        String oblocal;
        try{
            if(online){
                //Modo online
                //Consultar tablas
                estados = apiRest.getData("Estados");
                estudiante = apiRest.getData("Estudiante","ID,Identificacion,TipoIdentificacion","Identificacion:"+numIdent.getText().toString());
                actividades = apiRest.getData("Actividades","ID,Sesion_ID,Descripcion,Tiempo","Sesion_ID:"+ID);
                if(local){
                    //Actualizar las tablas Estudiantes, Actividad, Recursos, Estados
                    //Tabla Estado
                    for (int i=0; i<estados.length; i++)
                        localDB.Update(new Estado(Integer.parseInt(estados[i][0]),estados[i][1]),Integer.parseInt(estados[i][0]));
                    //Tabla Estudiante
                    localDB.Update(new Estudiante(Integer.parseInt(estudiante[0][0]),estudiante[0][1],estudiante[0][2]),Integer.parseInt(estudiante[0][0]));
                    //Tabla Actividades
                    for (int i=0; i<actividades.length; i++){
                        localDB.Update(new Actividad(Integer.parseInt(actividades[i][0]),Integer.parseInt(actividades[i][1]),actividades[i][2],
                                Integer.parseInt(actividades[i][3])),Integer.parseInt(actividades[i][0]));
                        //Tabla Recursos
                        recursos = apiRest.getData("Recursos","ID,Actividad_ID,Hipervinculo","Actividad_ID:"+actividades[i][0]);
                        for(int j=0; j<recursos.length; j++)
                            localDB.Update(new Recurso(Integer.parseInt(recursos[i][0]),Integer.parseInt(recursos[i][1]),recursos[i][2]),Integer.parseInt(recursos[i][0]));
                    }
                }
                else{
                    //Insertar datos en las tablas locales
                    //Tabla Estado
                    for (int i=0; i<estados.length; i++)
                        localDB.Insert(new Estado(Integer.parseInt(estados[i][0]),estados[i][1]));
                    //Tabla Estudiante
                    localDB.Insert(new Estudiante(Integer.parseInt(estudiante[0][0]),estudiante[0][1],estudiante[0][2]));
                    //Tabla Actividades
                    for (int i=0; i<actividades.length; i++){
                        localDB.Insert(new Actividad(Integer.parseInt(actividades[i][0]),Integer.parseInt(actividades[i][1]),actividades[i][2],Integer.parseInt(actividades[i][3])));
                        //Tabla Recursos
                        recursos = apiRest.getData("Recursos","ID,Actividad_ID,Hipervinculo","Actividad_ID:"+actividades[i][0]);
                        for(int j=0; j<recursos.length; j++)
                            localDB.Insert(new Recurso(Integer.parseInt(recursos[j][0]),Integer.parseInt(recursos[j][1]),recursos[j][2]));
                        //Tabla Estudiante_Actividad
                        tmp = apiRest.getData("Estudiantes_Actividades","Estudiante_ID,Actividad_ID,Estado_ID,Observaciones",
                                "Estudiante_ID:"+idEstudiante+"%20AND%20Actividad_ID:"+actividades[i][0]);
                        if(tmp.length==0)
                            tmp = apiRest.setData("Estudiantes_Actividades","Estudiante_ID,Actividad_ID,Estado_ID,Observaciones",
                                    idEstudiante+","+actividades[i][0]+","+estados[0][0]+",1@");
                        oblocal = tmp[0][3].split("@")[0]+"@"+tmp[0][3];
                        localDB.Insert(new Estudiante_Actividad(Integer.parseInt(tmp[0][0]),Integer.parseInt(tmp[0][1]),Integer.parseInt(tmp[0][2]),oblocal));
                    }
                }
                startActivity(in);
            }
            else{
                //Modo offline pero debe tener información en la base de datos local
                if(local){
                    Toast.makeText(getApplicationContext(), "Se encuentra en modo offline." +
                            "\nRecuerde sincronizar si cambia un estado.", Toast.LENGTH_SHORT).show();
                    startActivity(in);
                }
                else
                    Toast.makeText(getApplicationContext(), "No hay información local." +
                            "\nDebe conectarse previamente para modo offline.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error ApiRest: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Metodo para consultar los datos locales si hay
     * @return
     */
    private boolean local(){
        //Se consultan los datos de la base de datos local
        //Consultar que exista información de la sesion y del estudiante en la base de datos
        if(localDB.Read("Actividades","Sesion_ID="+ID).length>0){
            String[][] data = localDB.Read("Estudiantes","Identificacion="+numIdent.getText().toString());
            if(data.length>0){
                idEstudiante = data[0][0];
                return true;
            }
        }
        return false;
    }

    /**
     * Clase interna para probar la conexión y no bloquear la pantalla
     */
    private class ConnectTask extends AsyncTask<String, Float, Integer>{

        /**
         * Lo ejecuta el hilo principal antes de que inicie el hilo hijo
         * Deshabilita el boton de iniciar
         */
        @Override
        protected void onPreExecute() {
            btnSesion.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Conectando...", Toast.LENGTH_SHORT).show();
        }

        /**
         * Lo ejecuta el hilo hijo en segundo plano
         * Para probar la conexion y descargar los datos
         * @param params
         * @return
         */
        @Override
        protected Integer doInBackground(String[] params) {
            return connectMariaDB();
        }

        /**
         * Lo ejecuta el hilo hijo despues de terminar su codigo
         * Habilita el boton de iniciar y si se logra la conexion pone en true la variable connect
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {
            btnSesion.setEnabled(true);
            online = result==0;
            if(result==-2)
                Toast.makeText(MainActivity.context, "Datos incorrectos",Toast.LENGTH_SHORT).show();
            iniciar();
        }

        /**
         * Si se llega cancelar por alguna razon para el hilo hijo
         * Habilita el boton de descargar y cierra la ventana de dialogo
         * @param result
         */
        @Override
        protected void onCancelled(Integer result) {
            btnSesion.setEnabled(true);
            online = false;
            iniciar();
        }

        /**
         * Lo ejecuata el hilo hijo por si se quiere mostrar una barra de progreso
         * @param values
         */
        @Override
        protected void onProgressUpdate(Float... values) {
        }

        /**
         * Metodo para establecer la conexion con el servidor MariaDB por medio de la clase de ConexionApiRest
         */
        private int connectMariaDB(){
            //Me conecto con el servidor de base de datos
            String ftps, user, pass;
            try {
                apiRest = new ConexionApiRest(getString(R.string.mariadb));
                String[][] data = apiRest.getData( "FTPS");
                ftps = data[0][1];
                user = data[0][2];
                pass = data[0][3];
                if(connectFTPS(ftps,user,pass)){
                    //Validar que la sesion y el estudiante existe
                    if(apiRest.getData("Sesiones","ID","ID:"+ID).length == 1){
                        data = apiRest.getData("Estudiante","ID","Identificacion:"+numIdent.getText().toString());
                        if(data.length == 1){
                            idEstudiante = data[0][0];
                            return 0;
                        }
                        else
                            return -2;
                    }
                    else
                        return -2;
                }
            } catch (Exception e) {
                cancel(true);
                e.printStackTrace();
            }
            return -1;
        }

        /**
         * Metodo para establecer la conexion con el servidor SFTP por medio de la clase de ConexionFTPS
         */
        private boolean connectFTPS(String ftps, String user, String pass){
            try {
                MainActivity.ftps = new ConexionFTPS(ftps,user,pass);
                MainActivity.ftps.connect();
                MainActivity.ftps.disconnect();
                return true;
            } catch (Exception e) {
                cancel(true);
                e.printStackTrace();
            }
            return false;
        }
    }

}
