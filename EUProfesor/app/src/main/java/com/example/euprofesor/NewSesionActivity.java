package com.example.euprofesor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;

public class NewSesionActivity extends AppCompatActivity {

    private EditText proposito, fechaInicio, fechaCierre;
    private Spinner areas;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sesion);

        proposito = findViewById(R.id.edit_Proposito);
        areas = findViewById(R.id.sp_areas);
        fechaInicio = findViewById(R.id.edit_fechainicio);
        fechaCierre = findViewById(R.id.edit_fechacierre);
        btnAgregar = findViewById(R.id.btn_agregar);
    }

    
}