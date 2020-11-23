package com.thinkingahead.stockges.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.config.ConfiguracaoFireBase;

import java.util.Objects;

public class CriarContaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        // Configurar o Título da Barra Superior (ActionBar)
        Objects.requireNonNull(getSupportActionBar()).setTitle("Criar Conta");

        // Configurações Iniciais
        final EditText editEmail = findViewById(R.id.editTextCriarEmail);
        final EditText editPassword = findViewById(R.id.editTextCriarPassword);
        final EditText editConfirmarPassword = findViewById(R.id.editTextConfirmarPassword);
        final FirebaseAuth mAuth = ConfiguracaoFireBase.getFirebaseAuth();
        final EditText editNome = findViewById(R.id.editTextNomeUtilizador);
        Button buttonCriarConta = findViewById(R.id.buttonCriarConta);
        ImageButton imagePassword = findViewById(R.id.imageButtonPasswordCriarConta);

        // Criar Conta e enviar os Dados para a Firebase
        Configs.criarConta(buttonCriarConta, editNome, editEmail, editPassword, editConfirmarPassword, mAuth, this);

        // Mostrar ou Esconder Password
        Configs.esconderMostrarPassword(imagePassword, editPassword, editConfirmarPassword);

    }
}