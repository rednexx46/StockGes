package com.thinkingahead.stockges.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.model.Frases;

import java.util.List;

public class AdapterRecyclerFrases extends RecyclerView.Adapter<AdapterRecyclerFrases.MyViewHolder> {

    private final List<Frases> listaFrases;

    public AdapterRecyclerFrases(List<Frases> lista) {
        this.listaFrases = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_frases, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerFrases.MyViewHolder holder, int position) {

        Frases frase = listaFrases.get(position);
        holder.autor.setText("Autor: " + frase.getAutor());
        holder.frase.setText("Frase:\n" + frase.getFrase());

    }

    @Override
    public int getItemCount() {
        return listaFrases.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView frase, autor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            frase = itemView.findViewById(R.id.textFraseRemover);
            autor = itemView.findViewById(R.id.textAutor);

        }
    }

}
