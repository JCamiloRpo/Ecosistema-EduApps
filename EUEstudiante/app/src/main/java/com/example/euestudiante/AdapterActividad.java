package com.example.euestudiante;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class AdapterActividad extends RecyclerView.Adapter<AdapterActividad.MyViewHolderDatos> {

    ArrayList<Actividad> listDatos;

    public AdapterActividad(ArrayList<Actividad> listDatos) {
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
            btnDescargar = itemView.findViewById(R.id.btnDescargar);
            btnExpand = itemView.findViewById(R.id.btnExpand);
            btnEstado = itemView.findViewById(R.id.btnEstado);

            btnDescargar.setOnClickListener(new View.OnClickListener() {
                /**
                 * Descargar los recursos de la actividad seleccionada
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    String[][] data;
                    String ftpPaths="";
                    String localFile = "storage/emulated/0/FTP/Archivos";
                    String fileNames="";
                    try {
                        //Consultar las rutas de los archivos de la actividad selecionada
                        data = MainActivity.apiRest.getData("Recursos","Hipervinculo","Actividad_ID="+idActividad.getText().toString().replace("Actividad ",""));
                        for(int i=0; i< data.length; i++){
                            ftpPaths += data[i][0]+"@";
                            fileNames += data[i][0].split("/")[data[i][0].split("/").length-1]+"@";
                        }
                        //Se envia un vector donde cada posicion es la ruta del archivo y el nombre con el que se guardará
                        new SimpleTask(itemView, true).execute(localFile.split("@"),fileNames.split("@"),ftpPaths.split("@"));

                        idActividad.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.iniciada,0);

                    } catch (Exception e) {
                        Toast.makeText(itemView.getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

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

            btnEstado.setOnClickListener(new View.OnClickListener() {
                /**
                 * Mostar la ventana de dialogo para cambiar el estado de una actividad
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    TextView estadoActividad;
                    final Spinner spEstados;
                    Button btnCambiar, btnCancelar;

                    AlertDialog.Builder message = new AlertDialog.Builder(itemView.getContext());
                    View ventana = LayoutInflater.from(itemView.getContext()).inflate(R.layout.dialog_estado,null);
                    message.setView(ventana);
                    message.setCancelable(false);
                    final AlertDialog dialog = message.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    estadoActividad = ventana.findViewById(R.id.text_estadoactividad);
                    estadoActividad.setText("Cambiar estado "+idActividad.getText().toString());
                    spEstados = ventana.findViewById(R.id.spEstado);

                    btnCambiar = ventana.findViewById(R.id.btnCambiarEstado);
                    btnCambiar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(spEstados.getSelectedItemPosition()==0)
                                Toast.makeText(itemView.getContext(), "Debe selecionar un estado de actividad", Toast.LENGTH_SHORT).show();
                            else{
                                if(spEstados.getSelectedItemPosition()==1)
                                    idActividad.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.iniciada,0);
                                else if(spEstados.getSelectedItemPosition()==2)
                                    idActividad.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.finalizada,0);
                                else
                                    idActividad.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.abandonada,0);
                                dialog.dismiss();
                            }
                        }
                    });

                    btnCancelar = ventana.findViewById(R.id.btnCancelarEstado);
                    btnCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

            });
        }

    }
}
