package com.example.euprofesor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;

public class SesionActivity extends AppCompatActivity {

    public static String idSesion;
    private RecyclerView recyclerSesion;
    private ArrayList<Sesion> sesions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);

        permisos();
        recyclerSesion = findViewById(R.id.recylcer_sesiones);
        recyclerSesion.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        sesions = new ArrayList<>();
        consultar();
        AdapterSesion mAdapter = new AdapterSesion(sesions);
        recyclerSesion.setAdapter(mAdapter);
    }

    /**
     * Se llama al metodo cada que la actividad se vuelve a iniciar
     */
    @Override
    public void onStart() {
        super.onStart();
        sesions = new ArrayList<>();
        consultar();
        AdapterSesion mAdapter = new AdapterSesion(sesions);
        recyclerSesion.setAdapter(mAdapter);
    }

    /**
     * Se asigna el menu de sincronizar al activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    /**
     * Metodo para ir a la ventana para crear una sesion
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_add:
                Intent i = new Intent(this, NewSesionActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Obtener la tabla de sesiones, pero solo añadir a la lista las que cumplan con Area_ID = ID = 1
     */
    private void consultar(){
        String[][] data;

        try {
            //Consultar todas las sesiones del area 1
            data = MainActivity.apiRest.getData("Sesiones");
            for(int i=0; i< data.length; i++) {
                if (data[i][1].equals(MainActivity.idArea)) {// ID | Area_ID | Descripcion/Proposito | FechaInicio | FechaCierre
                    sesions.add(new Sesion(data[i][0], MainActivity.area, data[i][2], data[i][3], data[i][4]));
                }
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error ApiRest: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Metodo para para pedir los permisos de almacenamiento
     */
    private void permisos(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions,1);
            }
        }
    }
}
