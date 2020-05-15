package com.example.euprofesor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    public static ConexionFTPS ftps;
    public static ConexionApiRest apiRest;
    public static Context context;
    public static String idArea="0", area="";
    private Button btnSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        context = getApplicationContext();
        idArea = "1";
        btnSesion = findViewById(R.id.btn_sesion);
    }

    /**
     * Se validarian los campos, pero al ser un prototipo solo se consultan las sesiones del area con ID 1
     * @param v
     */
    public void btnSesion(View v){
        //Utilizar API para conexion con base de datos
        Intent i = new Intent(this, SesionActivity.class);
        new ConnectTask(i).execute();

    }

    private class ConnectTask extends AsyncTask<String, Float, Integer>{

        private Intent i;

        public ConnectTask(Intent i){
            this.i = i;
        }

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
            if(result==0)
                startActivity(i);
            else
                Toast.makeText(getApplicationContext(), "No se ha logrado establecer una conexión.", Toast.LENGTH_SHORT).show();
        }

        /**
         * Si se llega cancelar por alguna razon para el hilo hijo
         * Habilita el boton de descargar y cierra la ventana de dialogo
         * @param result
         */
        @Override
        protected void onCancelled(Integer result) {
            btnSesion.setEnabled(true);
            if(result==0)
                startActivity(i);
            else
                Toast.makeText(getApplicationContext(), "No se ha logrado establecer una conexión.", Toast.LENGTH_SHORT).show();
        }

        /**
         * Lo ejecuata el hilo hijo por si se quiere mostrar una barra de progreso
         * @param values
         */
        @Override
        protected void onProgressUpdate(Float... values) {
        }

        /**
         * Metodo para establecer la conexion con el servidor MariaDB por medio de la clase de ConexionMariaDB
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
                    //Validar que el area exite
                    data = apiRest.getData("Areas","Nombre","ID="+idArea);
                    if(data.length == 1) {
                        area = data[0][0];
                        return 0;
                    }
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
