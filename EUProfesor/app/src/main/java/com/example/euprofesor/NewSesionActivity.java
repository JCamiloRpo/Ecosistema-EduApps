package com.example.euprofesor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewSesionActivity extends AppCompatActivity {

    private EditText proposito, fechaInicio, fechaCierre;
    private Spinner areas;
    private ArrayList<String> nombreareas;
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

        nombreareas = new ArrayList<>();
        consultar();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, nombreareas);
        areas.setAdapter(adapter);
    }

    /**
     * Metodo para agregar una nueva sesion con 3 actividades y cada una con 3 recursos
     */
    public void btnAgregar(View v){
        String proposito = this.proposito.getText().toString();
        String fechaInicio = this.fechaInicio.getText().toString();
        String fechaCierre = this.fechaCierre.getText().toString();

        if(proposito.isEmpty())
            Toast.makeText(this,"Debe escribir un proposito",Toast.LENGTH_SHORT).show();
        else if(areas.getSelectedItemPosition()==0)
            Toast.makeText(this,"Debe seleccionar un area",Toast.LENGTH_SHORT).show();
        else if(fechaInicio.isEmpty())
            Toast.makeText(this,"Debe escribir una fecha de inicio",Toast.LENGTH_SHORT).show();
        else if(fechaCierre.isEmpty())
            Toast.makeText(this,"Debe escribir una fecha de cierre",Toast.LENGTH_SHORT).show();
        else if(validarFecha(fechaInicio))
            Toast.makeText(this, "La fecha de inicio no tiene el formato correcto (AAAA-MM-DD)", Toast.LENGTH_SHORT).show();
        else if(validarFecha(fechaCierre))
            Toast.makeText(this, "La fecha de cierre no tiene el formato correcto (AAAA-MM-DD)", Toast.LENGTH_SHORT).show();
        else{
            try {
                //Como las actividades de la sesion es estatica los archivos deben estar en la carpeta content del paquete SCORM
                String storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                String path = storage+"/FTP/MySCORM";
                File folderArchivos = new File(path+ "/content");
                if(!folderArchivos.exists()){
                    Toast.makeText(this,"Debe ubicar los archivos en: "+folderArchivos.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                    return;
                }
                //Inserto una nueva sesion
                String[][] data = MainActivity.apiRest.setData("Sesiones","ID,Area_ID,Descripcion,Fecha_Creacion,Fecha_Cierre",
                        "0,"+MainActivity.idArea+","+proposito.replaceAll(",","-")+","+fechaInicio+","+fechaCierre);

                if(data.length==0){
                    Toast.makeText(this,"No se pudo crear la sesion",Toast.LENGTH_SHORT).show();
                    return;
                }

                String remote="", names ="", local="", idSesion=data[0][0],nro, titulo, descripcion, tiempo;

                //Inicializar nuevo paquete SCORM
                MainActivity.scorm = new ConexionSCORM("SCORM EduApps",
                        "Este es el paquete SCORM creado desde codigo por medio de una plantilla",path);
                MainActivity.scorm.setInfo("ID Sesion: "+idSesion,
                        "<b>Proposito:</b> "+proposito+"<br><b>Profesor:</b> "+MainActivity.profesor+"<br><b>Area:</b>"+MainActivity.area);

                //Actividad 1
                nro="1";
                titulo="Medio ambiente";
                tiempo="10";
                descripcion="Vamos a conocer el medio ambiente!. Ver el video para conocer el medio ambiente," +
                        " luego la imagen con unos datos curiosos y por ultimo realizar la lectura del documento.";
                List<ConexionSCORM.Recurso> recursos = new ArrayList<>(Arrays.asList(
                        new ConexionSCORM.Recurso("¿Que es el medio ambiente?"," ",
                                "content/act1/ambiente.mp4","video/mp4","ambiente.mp4"),
                        new ConexionSCORM.Recurso("¿Sabias que?"," ",
                                "content/act1/imagen.jpg","imagen/jpg","imagen.jpg"),
                        new ConexionSCORM.Recurso("El agua","Realizar la lectura del siguiente documento:",
                                "content/act1/informacion.pdf","documento/pdf","informacion.pdf")));
                Toast.makeText(this,"Creando la actividad "+nro,Toast.LENGTH_SHORT).show();
                crearActividad(idSesion,nro,titulo,descripcion,tiempo,recursos);
                for(ConexionSCORM.Recurso r : recursos){
                    //Datos servidor FTP
                    remote+="/home/admin/www/"+MainActivity.profesor+"/"+idSesion+"-sesion@";
                    names+=nro+"-"+r.getName()+"@";
                    local+= storage+ "/FTP/MySCORM/content/act1/"+r.getName()+"@";
                }

                //Actividad 2
                nro="2";
                titulo="El agua";
                tiempo="20";
                descripcion="Ahora adentremonos en el agua y su ciclo, ademas conozcamos algunos retos sobre el cuidado del medio ambiente.";
                recursos = new ArrayList<>(Arrays.asList(
                        new ConexionSCORM.Recurso("Ciclo del agua","Entender el ciclo del agua.",
                                "content/act2/agua.jpg","imagen/jpg","agua.jpg"),
                        new ConexionSCORM.Recurso("El viaje de la gota del agua","Realizar la lectura del siguiente documento:",
                                "content/act2/agua.pdf","documento/pdf","agua.pdf"),
                        new ConexionSCORM.Recurso("Retos","Estos son algunos retos sobre el cuidado del medio ambiente:",
                                "content/act2/retos.mp4","video/mp4","retos.mp4")));
                Toast.makeText(this,"Creando la actividad "+nro,Toast.LENGTH_SHORT).show();
                crearActividad(idSesion,nro,titulo,descripcion,tiempo,recursos);
                for(ConexionSCORM.Recurso r : recursos){
                    //Datos servidor FTP
                    remote+="/home/admin/www/"+MainActivity.profesor+"/"+idSesion+"-sesion@";
                    names+=nro+"-"+r.getName()+"@";
                    local+= storage+ "/FTP/MySCORM/content/act2/"+r.getName()+"@";
                }

                //Actividad 3
                nro="3";
                titulo="Cuidar el medio ambiente";
                tiempo="15";
                descripcion="Cuidemos el medio ambiente!. Realizar la lectura del documento para conocer la acciones que podemos realizar," +
                        " luego instalar las aplicaciones, la primera nos brinda consejos para tener una casa sustentable y la segunda un" +
                        " seguimiento para realizar desafios ambientales.";
                recursos = new ArrayList<>(Arrays.asList(
                        new ConexionSCORM.Recurso("Cuidar el planeta","Conoce algunas cosas que puede hacer para cuidar el planeta",
                                "content/act3/acciones.pdf","documento/pdf","acciones.pdf"),
                        new ConexionSCORM.Recurso("Casa sustentable","Esta aplicacion te brindara consejos para cuidar el medio ambiente desde tu casa",
                                "content/act3/casa.apk","aplicacion/apk","casa.apk"),
                        new ConexionSCORM.Recurso("Desafia ambiental","Esta aplicacion te planteara unos desafios para cuidar el medio ambiente",
                                "content/act3/desafios.apk","aplicacion/apk","desafios.apk")));
                Toast.makeText(this,"Creando la actividad "+nro,Toast.LENGTH_SHORT).show();
                crearActividad(idSesion,nro,titulo,descripcion,tiempo,recursos);
                for(ConexionSCORM.Recurso r : recursos){
                    //Datos servidor FTP
                    remote+="/home/admin/www/"+MainActivity.profesor+"/"+idSesion+"-sesion@";
                    names+=nro+"-"+r.getName()+"@";
                    local+= storage+ "/FTP/MySCORM/content/act3/"+r.getName()+"@";
                }

                //Generar SCORM
                String tmp = MainActivity.scorm.generar(idSesion);
                remote+="/home/admin/www/"+MainActivity.profesor+"/"+idSesion+"-sesion@";
                names+="SCORM"+idSesion+".zip@";
                local+= tmp+"@";

                //Se envian los vectores con las rutas donde quedaran los archivos, los nombres
                new UploadTask().execute(remote.split("@"),names.split("@"), local.split("@"));

                Toast.makeText(this,"Se ha creado la sesion con ID: "+idSesion,Toast.LENGTH_SHORT).show();
                this.proposito.setText("");
                this.areas.setSelection(0);
                this.fechaInicio.setText("");
                this.fechaCierre.setText("");

            } catch (Exception e) {
                Toast.makeText(this,"Error al insertar: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }
    }

    /**
     * metodo para crear las actividades con sus recursos, tanto en el nodo como en el paquete SCORM
     * Necesario para la base de datos
     * @param idSesion
     * Necesarios para el paquete SCORM
     * @param nro
     * @param titulo
     * Necesarios para la base de datos y paquete SCORM
     * @param descripcion
     * @param tiempo
     * @param recursos es una lista de tipo ConexionSCORM.Recurso
     */
    public void crearActividad(String idSesion, String nro, String titulo, String descripcion, String tiempo, List<ConexionSCORM.Recurso> recursos)
            throws JSONException, IOException, InvalidKeyException, IllegalAccessException {
        //Crear la actividad
        String[][] data = MainActivity.apiRest.setData("Actividades","ID,Sesion_ID,Descripcion,Tiempo",
                "0,"+idSesion+","+descripcion.replaceAll(",","-")+","+tiempo);

        String idActividad = data[0][0];
        for(ConexionSCORM.Recurso r : recursos){
            //Crear los recursos
            String db="/home/admin/www/"+MainActivity.profesor+"/"+idSesion+"-sesion/"+nro+"-"+r.getName();
            MainActivity.apiRest.setData("Recursos","ID,Actividad_ID,Hipervinculo","0,"+idActividad+","+db);
        }
        //Agregar para el paquete SCORM
        MainActivity.scorm.addActividad("Actividad"+nro,titulo,descripcion+"<br><b>Tiempo:</b>"+tiempo+" minutos.",recursos);
    }

    /**
     * Metodo para validar el formato de la fecha AAAA-MM-DD o AAAA/MM/DD y los valores
     * @param fecha
     * @return
     */
    private boolean validarFecha(String fecha){
        String[] formato = fecha.split("-").length <  fecha.split("/") .length ? fecha.split("/") : fecha.split("-");
        return formato.length != 3 || formato[0].length() != 4 || formato[1].length() != 2
                || formato[2].length() != 2 || Integer.parseInt(formato[0]) < 0 || Integer.parseInt(formato[1]) < 0
                || Integer.parseInt(formato[1]) > 12 || Integer.parseInt(formato[2]) < 0 || Integer.parseInt(formato[2]) > 31;
    }

    /**
     * Metodo para consultar y establecer los tipos de areas
     */
    private void consultar(){
        try {
            String[][] data = MainActivity.apiRest.getData("Areas","Nombre");
            nombreareas.add("Areas");
            for (int i=0; i<data.length;i++)
                nombreareas.add(data[i][0]);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error ApiRest: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private class UploadTask extends AsyncTask<String[], Float, String> {

        /**
         * Lo ejecuta el hilo principal antes de que inicie el hilo hijo
         * Deshabilita el boton de agregar
         */
        @Override
        protected void onPreExecute() {
            try {
                btnAgregar.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Subiendo archivos", Toast.LENGTH_SHORT).show();
                MainActivity.ftps.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Lo ejecuta el hilo hijo en segundo plano
         * Empieza la carga de los n archivos
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(final String[][] params) {
            String resul="";
            try {
                float progreso=0.0f;
                for(int i=0; i<params[2].length; i++){//Iterar sobre cada ruta
                    MainActivity.ftps.addFile(params[0][i],params[1][i],params[2][i]);
                    progreso+=1;
                    publishProgress(progreso);
                }
            }catch (Exception e) {
                resul = "B"+e.getMessage();
                cancel(true);
                e.printStackTrace();
            }
            return resul;
        }

        /**
         * Lo ejecuta el hilo hijo despues de terminar su codigo
         * Habilita el boton de agregar
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                Toast.makeText(getApplicationContext(), "Carga exitosa", Toast.LENGTH_SHORT).show();
                MainActivity.ftps.disconnect();
                btnAgregar.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Si se llega cancelar por alguna razon para el hilo hijo
         * Habilita el boton de agregar
         * @param result
         */
        @Override
        protected void onCancelled(String result) {
            try {
                Toast.makeText(getApplicationContext(), "Se canceló la carga\n"+result, Toast.LENGTH_SHORT).show();
                MainActivity.ftps.disconnect();
                btnAgregar.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        /**
         * Lo ejecuata el hilo hijo por si se quiere mostrar una barra de progreso
         * @param values
         */
        @Override
        protected void onProgressUpdate(Float... values) {
        }
    }

    /*Codigo para elegir un archivo
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
            new UploadTask(new View(getApplicationContext()), false).execute(ftpPath.split("@"),name.split("@"),localFile.split("@"));
        }
    }
    /**/
}