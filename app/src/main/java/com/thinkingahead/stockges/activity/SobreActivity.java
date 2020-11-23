package com.thinkingahead.stockges.activity;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.thinkingahead.stockges.config.Configs;

import java.util.Objects;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Definir o Título da Barra Superior (ActionBar)
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sobre");

        // Ativar a "Seta" de Voltar Para Trás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configurar a Visualização para o Sobre
        View aboutPage = Configs.verificaoDoTema(getApplicationContext());

        // Definir a Visualização (Pré-Configurada) para o Sobre
        setContentView(aboutPage);

    }

}