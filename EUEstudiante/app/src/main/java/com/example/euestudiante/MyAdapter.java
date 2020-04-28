package com.example.euestudiante;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<String> listDatos;

    public MyAdapter(ArrayList<String> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.asignarDatos(listDatos.get(position));

    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dato;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dato = (TextView) itemView.findViewById(R.id.recycler_archivos);
        }

        public void asignarDatos(String datos) {
            dato.setText(datos);
        }
    }
}
