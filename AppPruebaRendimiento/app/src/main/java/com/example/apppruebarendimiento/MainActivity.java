package com.example.apppruebarendimiento;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ConexionFTP client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    public void BtnCargar(View v){
        if(client == null || !client.isConnected()){
            Toast.makeText(getApplicationContext(), "Debe establecer una conexión con el servidor", Toast.LENGTH_SHORT).show();
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions,1);
            }
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        i.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(i,"Choose File"),1);
    }

    public void BtnDescargar(View v){
        if(client == null || !client.isConnected()){
            Toast.makeText(getApplicationContext(), "Debe establecer una conexión con el servidor", Toast.LENGTH_SHORT).show();
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions,1);
            }
        }
        String remote = "/Prueba.zip", local ="/storage/emulated/0/Prueba.zip";
        try {
            if(client.Descargar(remote,local))
                Toast.makeText(getApplicationContext(), "El archivo se descargo exitosamente", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "El archivo no se pudo descargar", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error:"+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void BtnPreferences(View v){
        //Intent i = new Intent(this, Preferences.class);
        //startActivity(i);
        String ftp = "192.168.0.13"; // Se obtienen por medio de otra ventana
        String user = "root";
        String pas = "1234";
        client = new ConexionFTP(ftp,user,pas);
        try {
            if(client.Conectar())
                Toast.makeText(getApplicationContext(), "Conexión y logIn Exitoso", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "LogIn Erroneo"+client.getStatus(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(getApplicationContext(), "No se eligió un archivo", Toast.LENGTH_SHORT).show();

        if ((resultCode == RESULT_OK) && (requestCode == 1)) {
            //Procesar el resultado
            Uri uri = data.getData(); //obtener el uri content
            String path = GetPathUtil.getPath(getApplicationContext(), uri);
            File f = new File(path);
            try {
                if(client.Cargar(f))
                    Toast.makeText(getApplicationContext(), "El archivo se subido exitosamente", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "El archivo no se puedo subir", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error:"+e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

}
