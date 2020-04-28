package com.example.euestudiante;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ArchivoActivity extends AppCompatActivity {

    ArrayList<String> listDatos;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] myDataset = {"Archivo 1", "Archivo 2", "Archivo 3", "Archivo 4", "Archivo 5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_archivos);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listDatos = new ArrayList<String>();
        for (int i=0; i<=50; i++){
            listDatos.add("Dato # " +i+ " ");
        }


        recyclerView.setAdapter(mAdapter);
    }


}
