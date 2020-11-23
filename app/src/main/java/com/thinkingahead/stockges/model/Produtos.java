package com.thinkingahead.stockges.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.thinkingahead.stockges.config.Configs;

public class Produtos {
    private String codigo;
    private String descricao;
    private String quantidade;
    private String uniqueID;
    private String foto;

    public Produtos() {

    }

    @Exclude
    public void guardarDados() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Configs.getUserID()).child("produtos").child(uniqueID);
        databaseReference.setValue(this);

    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }


}
