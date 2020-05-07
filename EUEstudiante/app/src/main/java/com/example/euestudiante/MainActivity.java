package com.example.euestudiante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static ConexionFTPS client;
    public static String ID="0";
    public static Context context;
    private EditText idSesion, numIdent;
    private Spinner tipoIdent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        context = getApplicationContext();
        idSesion = findViewById(R.id.edit_sesion);
        tipoIdent = findViewById(R.id.spinner_tipoIdent);
        numIdent = findViewById(R.id.edit_numIdent);
    }

    public void btnSesion(View v){
        //Utilizar API para conexion con base de datos
        Intent i = new Intent(this, ActividadActivity.class);
        if(idSesion.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Debe escribir un ID de sesion", Toast.LENGTH_SHORT).show();
        else if(tipoIdent.getSelectedItemPosition()==0)
            Toast.makeText(getApplicationContext(), "Debe selecionar un tipo de identificación", Toast.LENGTH_SHORT).show();
        else if(numIdent.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Debe escribir un numero de identificación", Toast.LENGTH_SHORT).show();
        else{
            ID = idSesion.getText().toString();
            if(connectMariaDB())
                startActivity(i);
        }
    }

    /**
     * Metodo para establecer la conexion con el servidor MariaDB por medio de la clase de ConexionMariaDB
     */
    private boolean connectMariaDB(){
        //Me conecto con el servidor de base de datos
        String ftps, user, pass;
        try {
            String[][] data = ConexionApiRest.getData( getString(R.string.consulta)+"FTPS");
            ftps = data[0][1];
            user = data[0][2];
            pass = data[0][3];
            if(connectFTPS(ftps,user,pass)){
                //Validar que la sesion existe
                data = ConexionApiRest.getData(getString(R.string.consulta)+"Sesiones/Descripcion/?w=ID:"+idSesion.getText().toString());
                if(data.length == 1)
                    return true;
                else{
                    Toast.makeText(getApplicationContext(), "No existe la sesion con ID = "+idSesion.getText().toString(), Toast.LENGTH_SHORT).show();
                    return false;
                }

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
