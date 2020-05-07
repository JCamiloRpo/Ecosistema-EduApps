package com.example.euprofesor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class ActividadActivity extends AppCompatActivity {

    private String idSesion;
    private RecyclerView recyclerActividad;
    //private ArrayList<Actividad> actividads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);

        idSesion = SesionActivity.idSesion;
        recyclerActividad= findViewById(R.id.recycler_actividades);
        recyclerActividad.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

       /* actividads = new ArrayList<>();
        consultar();
        AdapterActividad mAdapter = new AdapterActividad(actividads);
        recyclerActividad.setAdapter(mAdapter);*/
    }

    /*private void consultar(){
        String[][] data,files;
        String fileNames;
        int nImg, nDoc, nApk, nVideo, nAudio, nOther;
        try {
            //Consultar todas las actividades
            data = MainActivity.apiRest.getData("Actividades");
            for(int i=0; i< data.length; i++){
                if(data[i][1].equals(idSesion)){// ID | Sesion_ID | Descripcion | Tiempo
                    files = MainActivity.apiRest.getData("Recursos","Hipervinculo","Actividad_ID="+ data[i][0]);
                    int k;
                    for(k=0, nImg=0, nDoc=0, nApk=0, nVideo=0, nAudio=0, nOther=0; k< files.length; k++){// Hipervinculo
                        fileNames = files[k][0].split("/")[files[k][0].split("/").length-1]; //Obtener solo el nombre

                        if(fileNames.endsWith(".png")||fileNames.endsWith(".PNG")||fileNames.endsWith(".jpg")||fileNames.endsWith(".JPG")||fileNames.endsWith(".jpeg") ||fileNames.endsWith(".JPEG"))
                            nImg++;
                        else if(fileNames.endsWith(".docx")||fileNames.endsWith(".DOCX")||fileNames.endsWith(".pdf")||fileNames.endsWith(".PDF")||fileNames.endsWith(".txt")||fileNames.endsWith(".TXT"))
                            nDoc++;
                        else if(fileNames.endsWith(".apk") || fileNames.endsWith(".APK"))
                            nApk++;
                        else if(fileNames.endsWith(".mp4")||fileNames.endsWith(".MP4")||fileNames.endsWith(".mov")||fileNames.endsWith(".MOV"))
                            nVideo++;
                        else if(fileNames.endsWith(".mp3")||fileNames.endsWith(".MP3")||fileNames.endsWith(".ogg")||fileNames.endsWith(".OGG")||fileNames.endsWith(".wav")||fileNames.endsWith(".WAV"))
                            nAudio++;
                        else
                            nOther++;
                    }
                    fileNames = nImg==0 ? "" : nImg==1 ? nImg+" imagen ":nImg+" imagenes ";
                    fileNames += nDoc==0 ? "" : nDoc==1 ? nDoc+" documento " : nDoc+" documentos ";
                    fileNames += nApk==0 ? "" : nApk==1 ? nApk+" aplicación " : nApk+" aplicaciones ";
                    fileNames += nVideo==0 ? "" : nVideo==1 ? nVideo+" video " : nVideo+" videos ";
                    fileNames += nAudio==0 ? "" : nAudio==1 ? nAudio+" audio " : nAudio+" audios ";
                    fileNames += nOther==0 ? "" : nOther==1 ? nOther+" otro archivo " : nOther+" otros archivos ";

                    actividads.add(new Actividad("Actividad "+(i+1), data[i][2], data[i][3]+" minutos", fileNames, (ImageButton) findViewById(R.id.btnExpand)));
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error ApiRest: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }*/
}
