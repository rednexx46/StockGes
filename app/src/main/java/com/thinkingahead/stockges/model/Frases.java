package com.thinkingahead.stockges.model;

import com.google.firebase.database.DatabaseReference;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.config.ConfiguracaoFireBase;

public class Frases {

    private String autor, frase, id;

    public Frases() {

    }

    public void guardar() {
        DatabaseReference databaseReference = ConfiguracaoFireBase.getDataBaseReference();
        databaseReference.child(Configs.getUserID()).child("frases").child(getId()).setValue(this);
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
