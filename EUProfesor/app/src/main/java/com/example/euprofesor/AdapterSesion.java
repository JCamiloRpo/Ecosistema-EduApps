package com.example.euprofesor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdapterSesion extends RecyclerView.Adapter<AdapterSesion.MyViewHolderDatos> {

    ArrayList<Sesion> listDatos;

    public AdapterSesion(ArrayList<Sesion> listDatos) {
        this.listDatos = listDatos;
    }

    @Override
    public AdapterSesion.MyViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_sesion, parent, false);
        return new MyViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(AdapterSesion.MyViewHolderDatos holder, int position) {
        holder.idSesion.setText(listDatos.get(position).getIdSesion());
        holder.proposito.setText(listDatos.get(position).getProposito());
        holder.area.setText(listDatos.get(position).getArea());
        holder.fechaInicio.setText(listDatos.get(position).getFechaInicio());
        holder.fechaCierre.setText(listDatos.get(position).getFechaCierre());
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class MyViewHolderDatos extends RecyclerView.ViewHolder {

        TextView idSesion, proposito, area, fechaInicio, fechaCierre;

        public MyViewHolderDatos(final View itemView) {
            super(itemView);

            idSesion = itemView.findViewById(R.id.text_idsesion);
            proposito = itemView.findViewById(R.id.text_proposito);
            area = itemView.findViewById(R.id.text_area);
            fechaInicio = itemView.findViewById(R.id.text_fechainicio);
            fechaCierre= itemView.findViewById(R.id.text_fechacierre);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SesionActivity.idSesion = idSesion.getText().toString();
                    Intent i = new Intent(itemView.getContext(), ActividadActivity.class);
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
