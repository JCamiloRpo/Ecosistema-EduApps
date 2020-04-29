package com.example.euestudiante;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;

public class ActividadActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Actividad> myDataset;
    private String idSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);

        permisos();
        idSesion = MainActivity.ID;
        recyclerView = findViewById(R.id.recycler_archivos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        myDataset = new ArrayList<>();
        consultar();
        MyAdapter mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);
    }


    private void consultar(){
        String[][] data,files;
        String fileNames;
        int nImg, nDoc, nApk, nVideo, nAudio, nOther;
        try {
            //Consultar todas las actividades
            data = ConexionApiRest.getData(getString(R.string.consulta)+"Actividades");
            for(int i=0; i< data.length; i++){
                if(data[i][1].equals(idSesion)){// ID | Sesion_ID | Descripcion | Tiempo
                    files = ConexionApiRest.getData(getString(R.string.consulta)+"Recursos/Hipervinculo/?w=Actividad_ID:"+ data[i][0]);
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

                    myDataset.add(new Actividad("Actividad "+(i+1), data[i][2], data[i][3]+" minutos", fileNames, (ImageButton)findViewById(R.id.btnDescargar)));
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

    /*Codigo para cargar archivos
    public void btnCargar(final View v){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("* /*");
        i.addCategory(Intent.CATEGORY_OPENABLE); //Abrir el Choose File del dispositivo movil
        startActivityForResult(Intent.createChooser(i,"Choose File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(getApplicationContext(), "No se eligió un archivo", Toast.LENGTH_SHORT).show();
        if ((resultCode == RESULT_OK) && (requestCode == 1)) {
            //Procesar el resultado
            Uri uri = data.getData(); //obtener el uri content
            String ftpPath = "/home/admin/wwww";
            String localFile = GetPathUtil.getPath(getApplicationContext(), uri);
            String name = new File(localFile).getName();
            new SimpleTask(new View(getApplicationContext()), false).execute(ftpPath.split("@"),name.split("@"),localFile.split("@"));
        }
    }
    /**/
}
