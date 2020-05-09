package com.example.euprofesor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterActividad extends RecyclerView.Adapter<AdapterActividad.MyViewHolderDatos> {

    ArrayList<Actividad> listDatos;

    public AdapterActividad(ArrayList<Actividad> listDatos) {
        this.listDatos = listDatos;
    }

    @Override
    public MyViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_actividad, parent, false);
        return new MyViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderDatos holder, int position) {
        holder.idActividad.setText(listDatos.get(position).getIdActividad());
        holder.descripcion.setText(listDatos.get(position).getDescripcion());
        holder.tiempo.setText(listDatos.get(position).getTiempo());
        holder.recurso.setText(listDatos.get(position).getRecurso());
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }


    public class MyViewHolderDatos extends RecyclerView.ViewHolder {

        TextView idActividad, descripcion, tiempo, recurso;
        ImageButton btnExpand;

        public MyViewHolderDatos(final View itemView) {
            super(itemView);
            idActividad = itemView.findViewById(R.id.text_actividad);
            descripcion = itemView.findViewById(R.id.text_contenido);
            tiempo = itemView.findViewById(R.id.text_tiempo);
            recurso = itemView.findViewById(R.id.text_recurso);
            btnExpand = itemView.findViewById(R.id.btnExpand);

            btnExpand.setOnClickListener(new View.OnClickListener() {
                private static final int DURATION = 250;
                private ViewGroup layoutDetalle =itemView.findViewById(R.id.layoutDetalles);

                @Override
                public void onClick(View v) {
                    if (layoutDetalle.getVisibility() == View.GONE) {
                        ExpandAndCollapseViewUtil.expand(layoutDetalle, DURATION);
                        btnExpand.setImageResource(R.drawable.more);
                        rotate(180.0f);
                    } else {
                        ExpandAndCollapseViewUtil.collapse(layoutDetalle, DURATION);
                        btnExpand.setImageResource(R.drawable.less);
                        rotate(-180.0f);
                    }
                }

                private void rotate(float angle) {
                    Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setFillAfter(true);
                    animation.setDuration(DURATION);
                    btnExpand.startAnimation(animation);
                }
            });

        }

    }
}
