package com.thinkingahead.stockges.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;

import java.util.Objects;

public class AdicionarFraseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_frase);

        // Definir Título da Barra Superior (ActionBar)
        Objects.requireNonNull(getSupportActionBar()).setTitle("Adicionar Frase");

        // Configurações Iniciais
        EditText editAutor = findViewById(R.id.editTextAutor);
        EditText editFrase = findViewById(R.id.editTextFrase);
        Button buttonConfirmar = findViewById(R.id.buttonConfirmarFrase);

        // Adicionar Frase e o Respetivo Autor para dentro da base de dados na última posição
        Configs.adicionarFrase(buttonConfirmar, editAutor, editFrase, getApplicationContext(), this);


    }
}