package com.example.euestudiante;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.euestudiante.entidades.Estudiante_Actividad;

import java.security.InvalidKeyException;
import java.util.ArrayList;

public class ActividadActivity extends AppCompatActivity {

    private RecyclerView recyclerActividad;
    private ArrayList<ActividadView> actividads;
    private String idSesion;
    public static MenuItem btnSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);

        permisos();
        idSesion = MainActivity.ID;
        recyclerActividad = findViewById(R.id.recycler_actividad);
        recyclerActividad.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        actividads = new ArrayList<>();
        consulta();
        AdapterActividad mAdapter = new AdapterActividad(actividads);
        recyclerActividad.setAdapter(mAdapter);

    }

    /**
     * Se asigna el menu de sincronizar al activity
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sync, menu);
        btnSync = menu.findItem(R.id.action_sync);
        btnSync.setEnabled(true);
        return true;
    }

    /**
     * Metodo cuando se unde en el menú
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_sync:
                if (MainActivity.apiRest.isConnected()) {
                    actividads = new ArrayList<>();
                    AdapterActividad mAdapter = new AdapterActividad(actividads);
                    recyclerActividad.setAdapter(mAdapter);
                    consulta();
                    Toast.makeText(getApplicationContext(), "Estados sincronizados.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "No puede sincronizar los estados porque está en modo offline.", Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Consultar las actividades de la sesion para ponerlas en el RecyclerView
     */
    private void consulta() {
        String[][] data, files, remota, local;
        String fileNames, nameEstado;
        int nImg, nDoc, nApk, nVideo, nAudio, nOther, estado;
        try {
            //Consultar todas las actividades
            data = MainActivity.localDB.Read("Actividades", "Sesion_ID=" + idSesion);

            for (int i = 0; i < data.length; i++) {
                files = MainActivity.localDB.Read("Recursos", "Hipervinculo", "Actividad_ID=" + data[i][0]);

                int k;
                for (k = 0, nImg = 0, nDoc = 0, nApk = 0, nVideo = 0, nAudio = 0, nOther = 0; k < files.length; k++) {// Hipervinculo
                    fileNames = files[k][0].split("/")[files[k][0].split("/").length - 1]; //Obtener solo el nombre

                    if (fileNames.endsWith(".png") || fileNames.endsWith(".PNG") || fileNames.endsWith(".jpg") || fileNames.endsWith(".JPG") ||
                            fileNames.endsWith(".jpeg") || fileNames.endsWith(".JPEG"))
                        nImg++;
                    else if (fileNames.endsWith(".docx") || fileNames.endsWith(".DOCX") || fileNames.endsWith(".pdf") || fileNames.endsWith(".PDF") ||
                            fileNames.endsWith(".txt") || fileNames.endsWith(".TXT"))
                        nDoc++;
                    else if (fileNames.endsWith(".apk") || fileNames.endsWith(".APK"))
                        nApk++;
                    else if (fileNames.endsWith(".mp4") || fileNames.endsWith(".MP4") || fileNames.endsWith(".mov") || fileNames.endsWith(".MOV"))
                        nVideo++;
                    else if (fileNames.endsWith(".mp3") || fileNames.endsWith(".MP3") || fileNames.endsWith(".ogg") || fileNames.endsWith(".OGG") ||
                            fileNames.endsWith(".wav") || fileNames.endsWith(".WAV"))
                        nAudio++;
                    else
                        nOther++;
                }
                if (files.length > 0) {
                    fileNames = nImg == 0 ? "" : nImg == 1 ? nImg + " imagen " : nImg + " imagenes ";
                    fileNames += nDoc == 0 ? "" : nDoc == 1 ? nDoc + " documento " : nDoc + " documentos ";
                    fileNames += nApk == 0 ? "" : nApk == 1 ? nApk + " aplicación " : nApk + " aplicaciones ";
                    fileNames += nVideo == 0 ? "" : nVideo == 1 ? nVideo + " video " : nVideo + " videos ";
                    fileNames += nAudio == 0 ? "" : nAudio == 1 ? nAudio + " audio " : nAudio + " audios ";
                    fileNames += nOther == 0 ? "" : nOther == 1 ? nOther + " otro archivo " : nOther + " otros archivos ";
                } else {
                    fileNames = "No hay archivos.";
                }

                //Consultar el estado de la actividad del estudiante en el dispositivo
                local = MainActivity.localDB.Read("Estudiantes_Actividades", "Estado_ID,Observaciones",
                        "Estudiante_ID=" + MainActivity.idEstudiante + " AND Actividad_ID=" + data[i][0]);
                if (MainActivity.apiRest.isConnected()) {
                    //Consultar el estado de la actividad del estudiante en el nodo
                    remota = MainActivity.apiRest.getData("Estudiantes_Actividades", "Estado_ID,Observaciones",
                            "Estudiante_ID:" + MainActivity.idEstudiante + "%20AND%20Actividad_ID:" + data[i][0]);
                    //Sincronizar ambos estados
                    estado = Sincronizar(MainActivity.idEstudiante, data[i][0], remota[0][0], local[0][0], remota[0][1], local[0][1]);
                } else
                    estado = Integer.parseInt(local[0][0]);

                nameEstado = MainActivity.localDB.Read("Estados","Descripcion","ID="+estado)[0][0];

                actividads.add(new ActividadView(data[i][0] + "@Actividad " + (i + 1), data[i][2], data[i][3] + " minutos", fileNames,
                        (ImageButton) findViewById(R.id.btnDescargar), (ImageButton) findViewById(R.id.btnExpand), (ImageButton) findViewById(R.id.btnEstado), nameEstado));
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Metodo para sincronizar los estados de las actividades
     */
    private int Sincronizar(String estudiante, String actividad, String estremota, String estlocal, String obremota, String oblocal) {
        int versionOriginal, versionNueva, versionRemota;
        int estado = Integer.parseInt(estremota);
        try {
            //Comparar las versiones locales con las remotas
            versionOriginal = Integer.parseInt(oblocal.split("@")[0]);
            versionNueva = Integer.parseInt(oblocal.split("@")[1]);
            versionRemota = Integer.parseInt(obremota.split("@")[0]);
            if (versionNueva > versionRemota) {
                //Actualizo la remota
                obremota = oblocal.replace(versionOriginal+"@","");
                MainActivity.apiRest.updateData("Estudiantes_Actividades", "Estado_ID", estlocal,
                        "Estudiante_ID=" + estudiante + " AND Actividad_ID=" + actividad);
                MainActivity.apiRest.updateData("Estudiantes_Actividades", "Observaciones", obremota,
                        "Estudiante_ID=" + estudiante + " AND Actividad_ID=" + actividad);

                //Actualizo los metadatos local
                oblocal = versionNueva + "@" + obremota;
                MainActivity.localDB.Update(new Estudiante_Actividad(Integer.parseInt(estudiante), Integer.parseInt(actividad),
                        Integer.parseInt(estlocal), oblocal), Integer.parseInt(estudiante), Integer.parseInt(actividad));
                estado = Integer.parseInt(estlocal);
            } else if (versionNueva < versionRemota || (versionNueva == versionRemota && versionOriginal < versionRemota)) {
                //Actualizo la local
                oblocal = versionRemota + "@" + obremota;
                MainActivity.localDB.Update(new Estudiante_Actividad(Integer.parseInt(estudiante), Integer.parseInt(actividad),
                        Integer.parseInt(estremota), oblocal), Integer.parseInt(estudiante), Integer.parseInt(actividad));

                estado = Integer.parseInt(estremota);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Solo cuando llega hasta acá quiere decir que los estados se sincronizaron
        if(btnSync!=null)
            btnSync.setEnabled(false);
        return estado;
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
