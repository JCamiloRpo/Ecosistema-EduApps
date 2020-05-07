package com.example.euprofesor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static ConexionFTPS client;
    public static ConexionApiRest apiRest;
    public static Context context;
    public static String idArea="0", area="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        context = getApplicationContext();
        idArea = "1";

    }

    /**
     * Se validarian los campos, pero al ser un prototipo solo se consultan las sesiones del area con ID 1
     * @param v
     */
    public void btnSesion(View v){
        //Utilizar API para conexion con base de datos
        Intent i = new Intent(this, SesionActivity.class);
        if(connectMariaDB())
            startActivity(i);
    }

    /**
     * Metodo para establecer la conexion con el servidor MariaDB por medio de la clase de ConexionMariaDB
     */
    private boolean connectMariaDB(){
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
                data = apiRest.getData("areas","Nombre","ID="+idArea);
                if(data.length == 1) {
                    area = data[0][0];
                    return true;
                }
                Toast.makeText(getApplicationContext(), "No existe el area con ID = "+idArea, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error ApiRest: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo para establecer la conexion con el servidor SFTP por medio de la clase de ConexionFTPS
     */
    private boolean connectFTPS(String ftps, String user, String pass){
        try {
            client = new ConexionFTPS(ftps,user,pass);
            client.connect();
            client.disconnect();
            return true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error FTPS: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }

}
