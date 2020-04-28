package com.example.euestudiante;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActividadActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] myDataset = {"Actividad 1", "Actividad 2", "Actividad 3", "Actividad 4", "Actividad 5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_actividad);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);
    }

    public void BtnArchivo(View v){
        /*Al dar click en un archivo abrir la actividad de archivo*/
        Intent i = new Intent(this, ArchivoActivity.class);
        startActivity(i);

    }

    public void BtnDescargar(View v){
        /*Descargar el contenido de todas las actividades*/
    }
}
