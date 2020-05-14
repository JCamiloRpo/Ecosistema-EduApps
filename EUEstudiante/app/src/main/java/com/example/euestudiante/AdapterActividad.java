package com.example.euestudiante;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.euestudiante.entidades.Actividad;
import com.example.euestudiante.entidades.Estado;
import com.example.euestudiante.entidades.Estudiante_Actividad;

import java.io.IOException;
import java.util.ArrayList;

public class AdapterActividad extends RecyclerView.Adapter<AdapterActividad.MyViewHolderDatos> {

    ArrayList<ActividadView> listDatos;

    public AdapterActividad(ArrayList<ActividadView> listDatos) {
        this.listDatos = listDatos;
    }

    @Override
    public MyViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new MyViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderDatos holder, int position) {
        holder.idActividad.setText(listDatos.get(position).getIdActividad().split("@")[1]);
        switch (listDatos.get(position).getEstado()){
            case "Iniciada": holder.idActividad.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.iniciada,0);
                break;
            case "Finalizada": holder.idActividad.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.finalizada,0);
                break;
            case "Abandonada": holder.idActividad.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.abandonada,0);
                break;
            default:
        }
        holder.descripcion.setText(listDatos.get(position).getDescripcion());
        holder.tiempo.setText(listDatos.get(position).getTiempo());
        holder.recurso.setText(listDatos.get(position).getRecurso());
        holder.btnDescargar = listDatos.get(position).getBtnDescarga();
        holder.btnEstado = listDatos.get(position).getBtnEstado();
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class MyViewHolderDatos extends RecyclerView.ViewHolder {

        TextView idActividad, descripcion, tiempo, recurso;
        ImageButton btnDescargar, btnExpand, btnEstado;

        public MyViewHolderDatos(final View itemView) {
            super(itemView);
            idActividad = itemView.findViewById(R.id.text_actividad);
            descripcion = itemView.findViewById(R.id.text_contenido);
            tiempo = itemView.findViewById(R.id.text_tiempo);
            recurso = itemView.findViewById(R.id.text_recurso);
            btnExpand = itemView.findViewById(R.id.btnExpand);
            ExpandAndCollap();
            btnEstado = itemView.findViewById(R.id.btnEstado);
            CambiarEstado();
            btnDescargar = itemView.findViewById(R.id.btnDescargar);
            Descargar();

        }

        /**
         * Metodo para definir el onClick para ExpandAndCollap y collapsar las CardView
         */
        private void ExpandAndCollap(){
            btnExpand.setOnClickListener(new View.OnClickListener() {
                private static final int DURATION = 250;
                private ViewGroup layoutDetalle =itemView.findViewById(R.id.layoutDetalles);

                /**
                 * Expandir o colapsar la información de recursos
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    if (layoutDetalle.getVisibility() == View.GONE) {//Se expante la tarjeta
                        ExpandAndCollapseViewUtil.expand(layoutDetalle, DURATION);
                        btnExpand.setImageResource(R.drawable.more);
                        rotate(180.0f);
                    } else { //Se oculta la tarjeta
                        ExpandAndCollapseViewUtil.collapse(layoutDetalle, DURATION);
                        btnExpand.setImageResource(R.drawable.less);
                        rotate(-180.0f);
                    }
                }

                private void rotate(float angle) {//Animación de la rotación
                    Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setFillAfter(true);
                    animation.setDuration(DURATION);
                    btnExpand.startAnimation(animation);
                }
            });

        }

        /**
         * Metodo para definir el onClick para cambiar de estado
         */
        private void CambiarEstado(){
            btnEstado.setOnClickListener(new View.OnClickListener() {
                private Spinner spEstados;
                private TextView estadoActividad;
                private EditText observacion;
                private Button btnCambiar, btnCancelar;
                private AlertDialog dialog;
                /**
                 * Mostar la ventana de dialogo para cambiar el estado de una actividad
                 */
                @Override
                public void onClick(View v) {
                    alert();
                    btnCambiar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (spEstados.getSelectedItem().toString()){
                                case "Iniciada": idActividad.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.iniciada,0);
                                    break;
                                case "Finalizada": idActividad.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.finalizada,0);
                                    break;
                                case "Abandonada": idActividad.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.abandonada,0);
                                    break;
                                default:
                                    Toast.makeText(itemView.getContext(), "Debe selecionar un estado de actividad", Toast.LENGTH_SHORT).show();
                            }
                            ActualizarEstado();
                            dialog.dismiss();
                        }
                    });
                    btnCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

                private void alert(){
                    try{
                        AlertDialog.Builder message = new AlertDialog.Builder(itemView.getContext());
                        final View ventana = LayoutInflater.from(itemView.getContext()).inflate(R.layout.dialog_estado,null);
                        message.setView(ventana);
                        message.setCancelable(false);
                        dialog = message.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        observacion = ventana.findViewById(R.id.edit_Observacion);
                        estadoActividad = ventana.findViewById(R.id.text_estadoactividad);
                        estadoActividad.setText("Cambiar estado "+idActividad.getText().toString());
                        spEstados = ventana.findViewById(R.id.spEstado);

                        ArrayList<String> estados = new ArrayList<>();
                        String[][] data = MainActivity.localDB.Read("Estados");
                        estados.add("Seleccionar Estado");
                        for (int i=0; i<data.length; i++)
                            estados.add(data[i][1]);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ventana.getContext(), android.R.layout.simple_spinner_item, estados);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spEstados.setAdapter(adapter);
                        spEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(position==3)
                                    ventana.findViewById(R.id.edit_Observacion).setVisibility(View.VISIBLE);
                                else
                                    ventana.findViewById(R.id.edit_Observacion).setVisibility(View.INVISIBLE);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        btnCambiar = ventana.findViewById(R.id.btnCambiarEstado);
                        btnCancelar = ventana.findViewById(R.id.btnCancelarEstado);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                private void ActualizarEstado(){
                    //Actualizar estado local
                    int pos = Integer.parseInt(idActividad.getText().toString().replace("Actividad ",""))-1;
                    String idActividad = listDatos.get(pos).getIdActividad().split("@")[0];
                    String[][] local = MainActivity.localDB.Read("Estudiantes_Actividades","Estado_ID,Observaciones",
                            "Estudiante_ID="+MainActivity.idEstudiante+" AND Actividad_ID="+idActividad);
                    String[][] estado = MainActivity.localDB.Read("Estados","Descripcion","ID="+local[0][0]);

                    String oblocal=local[0][1];
                    String[] tmp = oblocal.split("@");
                    int versionOriginal = Integer.parseInt(tmp[0]);
                    int versionNueva = versionOriginal+1;

                    if(estado[0][0].equals(spEstados.getSelectedItem().toString()) &&
                            oblocal.equals(versionOriginal+"@"+versionOriginal+"@"+observacion.getText().toString()))
                        return;//Solo se actualiza si se escoge un estado diferente y la observacion es diferente

                    oblocal = versionOriginal+"@"+versionNueva+"@"+observacion.getText().toString();
                    MainActivity.localDB.Update(new Estudiante_Actividad(Integer.parseInt(MainActivity.idEstudiante),Integer.parseInt(idActividad),
                            spEstados.getSelectedItemPosition(),oblocal),Integer.parseInt(MainActivity.idEstudiante),Integer.parseInt(idActividad));

                    //Actualizar estado remoto si estoy online
                    if(MainActivity.online){
                        try {
                            String[][] remota = MainActivity.apiRest.getData("Estudiantes_Actividades","Estado_ID,Observaciones",
                                    "Estudiante_ID:"+MainActivity.idEstudiante+"%20AND%20Actividad_ID:"+idActividad);
                            estado = MainActivity.apiRest.getData("Estados","Descripcion","ID:"+remota[0][0]);

                            if(estado[0][0].equals(spEstados.getSelectedItem().toString()) &&
                                    remota[0][1].equals(versionOriginal+"@"+observacion.getText().toString())){
                                //Vuelvo la local igual a la remota porque no se cambio realmente de estado ni la observación
                                oblocal =  remota[0][1].split("@")[0]+"@"+remota[0][1];
                                MainActivity.localDB.Update(new Estudiante_Actividad(Integer.parseInt(MainActivity.idEstudiante),Integer.parseInt(idActividad),
                                        Integer.parseInt(estado[0][0]),oblocal),Integer.parseInt(MainActivity.idEstudiante),Integer.parseInt(idActividad));
                                return;
                            }
                            //Actualizo el estado
                            String obremota=versionNueva+"@"+observacion.getText().toString();;
                            MainActivity.apiRest.updateData("Estudiantes_Actividades","Estado_ID",spEstados.getSelectedItemPosition()+"",
                                    "Estudiante_ID:"+MainActivity.idEstudiante+"%20AND%20Actividad_ID:"+idActividad);
                            MainActivity.apiRest.updateData("Estudiantes_Actividades","Observaciones",obremota,
                                    "Estudiante_ID:"+MainActivity.idEstudiante+"%20AND%20Actividad_ID:"+idActividad);

                            //Actualizo la observacion local
                            oblocal=versionNueva+"@"+obremota;
                            MainActivity.localDB.Update(new Estudiante_Actividad(Integer.parseInt(MainActivity.idEstudiante),Integer.parseInt(idActividad),
                                    spEstados.getSelectedItemPosition(),oblocal),Integer.parseInt(MainActivity.idEstudiante),Integer.parseInt(idActividad));
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ActividadActivity.sync = false;

                    Toast.makeText(itemView.getContext(), "No olvide sincronizar el estado cuando tenga conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }

        /**
         * Metodo para establecer el onClick para descargar los archivos
         */
        private void Descargar(){
            btnDescargar.setOnClickListener(new View.OnClickListener() {
                /**
                 * Descargar los recursos de la actividad seleccionada
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    String[][] data;
                    String ftpPaths="";
                    String localFile = Environment.getExternalStorageDirectory()+"/FTP/Archivos";
                    String fileNames="";
                    try {
                        if(!MainActivity.online){
                            Toast.makeText(itemView.getContext(),"Debe estar online para descargar el contenido",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Consultar las rutas de los archivos de la actividad selecionada
                        int pos = Integer.parseInt(idActividad.getText().toString().replace("Actividad ",""))-1;
                        String id = listDatos.get(pos).getIdActividad().split("@")[0];
                        data = MainActivity.localDB.Read("Recursos","Hipervinculo","Actividad_ID="+id);
                        for(int i=0; i< data.length; i++){
                            ftpPaths += data[i][0]+"@";
                            fileNames += data[i][0].split("/")[data[i][0].split("/").length-1]+"@";
                        }
                        //Se envia un vector donde cada posicion es la ruta del archivo y el nombre con el que se guardará
                        new DownloadTask().execute(localFile.split("@"),fileNames.split("@"),ftpPaths.split("@"));

                    } catch (Exception e) {
                        Toast.makeText(itemView.getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        }

        private class DownloadTask extends AsyncTask<String[], Float, String> {

            private AlertDialog dialog;

            /**
             * Lo ejecuta el hilo principal antes de que inicie el hilo hijo
             * Deshabilita el boton de descarga y muestra la ventana de dialogo
             */
            @Override
            protected void onPreExecute() {
                try {
                    alert();
                    MainActivity.ftps.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * Lo ejecuta el hilo hijo en segundo plano
             * Empieza la descargar de los n archivos
             * @param params
             * @return
             */
            @Override
            protected String doInBackground(final String[][] params) {
                String resul="";
                try {
                    float progreso=0.0f;
                    for(int i=0; i<params[2].length; i++){//Iterar sobre cada ruta
                        MainActivity.ftps.downloadFile(params[0][0],params[1][i],params[2][i]);
                        progreso+=1;
                        publishProgress(progreso);
                    }
                } catch (Exception e) {
                    resul = e.getMessage();
                    cancel(true);
                    e.printStackTrace();
                }
                return resul;
            }

            /**
             * Lo ejecuta el hilo hijo despues de terminar su codigo
             * Habilita el boton de descargar y cierra la ventana de dialogo
             * @param result
             */
            @Override
            protected void onPostExecute(String result) {
                try {
                    Toast.makeText(itemView.getContext(), "Descarga exitosa", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    MainActivity.ftps.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /**
             * Si se llega cancelar por alguna razon para el hilo hijo
             * Habilita el boton de descargar y cierra la ventana de dialogo
             * @param result
             */
            @Override
            protected void onCancelled(String result) {
                try {
                    Toast.makeText(itemView.getContext(), "Se canceló la descargar", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    MainActivity.ftps.disconnect();
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

            /**
             * Metodo auxiliar para mostar la ventana de dialogo por si se desea cancelar la descarga
             */
            private void alert(){
                TextView actividad, id, descripcion;
                Button btnCancelar;

                id = itemView.findViewById(R.id.text_actividad);

                AlertDialog.Builder message = new AlertDialog.Builder(itemView.getContext());
                View ventana = LayoutInflater.from(itemView.getContext()).inflate(R.layout.dialog_descarga,null);
                message.setView(ventana);
                message.setCancelable(false);
                dialog = message.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                actividad = ventana.findViewById(R.id.text_actividad);
                actividad.setText(id.getText().toString());
                descripcion = ventana.findViewById(R.id.text_descripcion);
                descripcion.setText("Descargando los recursos de "+actividad.getText().toString()+"...");
                btnCancelar = ventana.findViewById(R.id.btnCancelar);
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel(true);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    }
}
