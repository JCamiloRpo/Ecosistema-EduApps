package com.example.euprofesor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NewSesionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sesion);
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