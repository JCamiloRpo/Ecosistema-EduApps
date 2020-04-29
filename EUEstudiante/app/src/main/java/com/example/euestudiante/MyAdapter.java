package com.example.euestudiante;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolderDatos> {

    ArrayList<Actividad> listDatos;

    public MyAdapter(ArrayList<Actividad> listDatos) {
        this.listDatos = listDatos;
    }

    @Override
    public MyViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new MyViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderDatos holder, int position) {
        holder.idActividad.setText(listDatos.get(position).getIdActividad());
        holder.descripcion.setText(listDatos.get(position).getDescripcion());
        holder.tiempo.setText(listDatos.get(position).getTiempo());
        holder.recurso.setText(listDatos.get(position).getRecurso());
        holder.btnDescargar = listDatos.get(position).getBtnDescarga();
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }


    public class MyViewHolderDatos extends RecyclerView.ViewHolder {

        TextView idActividad, descripcion, tiempo, recurso;
        ImageButton btnDescargar;

        public MyViewHolderDatos(final View itemView) {
            super(itemView);
            idActividad = itemView.findViewById(R.id.text_actividad);
            descripcion = itemView.findViewById(R.id.text_contenido);
            tiempo = itemView.findViewById(R.id.text_tiempo);
            recurso = itemView.findViewById(R.id.text_recurso);
            btnDescargar = itemView.findViewById(R.id.btnDescargar);

            btnDescargar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Descargar el contenido de todas las actividades*/
                    String[][] tmp;
                    String ftpPaths="";
                    String localFile = "storage/emulated/0/FTP/Archivos";
                    String fileNames="";
                    try {
                        //Consultar las rutas de los archivos de la actividad selecionada
                        tmp = ConexionApiRest.getData(MainActivity.context.getString(R.string.consulta)+"Recursos/Hipervinculo/?w=Actividad_ID:"+idActividad.getText().toString().replace("Actividad ",""));
                        for(int i=0; i< tmp.length; i++){
                            ftpPaths += tmp[i][0]+"@";
                            fileNames += tmp[i][0].split("/")[tmp[i][0].split("/").length-1]+"@";
                        }
                        //Se envia un vector donde cada posicion es la ruta del archivo y el nombre con el que se guardará
                        new SimpleTask(itemView, true).execute(localFile.split("@"),fileNames.split("@"),ftpPaths.split("@"));

                    } catch (Exception e) {
                        Toast.makeText(itemView.getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

        }

    }
}
