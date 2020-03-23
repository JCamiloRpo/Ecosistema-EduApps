package com.example.apppruebarendimiento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class ToolsActivity extends AppCompatActivity {

    private EditText direccionIp, usuario, contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        loadData();
    }

    private void loadData(){
        if(MainActivity.client == null) //Si no hay conexion se dejan los campos vacios, si ya hay una se llenan
            return;
        direccionIp = (EditText) findViewById(R.id.edit_ip);
        usuario= (EditText) findViewById(R.id.edit_user);
        contraseña = (EditText) findViewById(R.id.edit_password);
        direccionIp.setText(MainActivity.client.getFtp());
        usuario.setText(MainActivity.client.getUser());
        contraseña.setText(MainActivity.client.getPas());
    }

    public void BtnConectar(View v){
        try {
            direccionIp = (EditText) findViewById(R.id.edit_ip);
            usuario = (EditText) findViewById(R.id.edit_user);
            contraseña = (EditText) findViewById(R.id.edit_password);
            if (direccionIp.getText().toString().isEmpty()) { //Validaciones de nulos
                Toast.makeText(this, "Debe ingresar la Ip del servidor FTP", Toast.LENGTH_LONG).show();
            } else if (usuario.getText().toString().isEmpty()) {
                Toast.makeText(this, "Debe ingresar el usuario del servidor FTP", Toast.LENGTH_LONG).show();
            } else if (contraseña.getText().toString().isEmpty()) {
                Toast.makeText(this, "Debe ingresar la contraseña del servidor FTP", Toast.LENGTH_LONG).show();
            } else {//Se crea la conexion
                MainActivity.client = new ConexionFTP(direccionIp.getText().toString(), usuario.getText().toString(), contraseña.getText().toString());

                if (MainActivity.client.Conectar()) {
                    Toast.makeText(getApplicationContext(), "Conexión y logIn Exitoso", Toast.LENGTH_SHORT).show();
                    MainActivity.client.Desconectar();//se desconecta
                    finish(); //Se devuelve a la actividad principal
                } else
                    Toast.makeText(getApplicationContext(), "LogIn Erroneo: " + MainActivity.client.getStatus(), Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
