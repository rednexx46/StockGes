package com.thinkingahead.stockges.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.config.ConfiguracaoFireBase;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Redefinir Título da Barra Superior (ActionBar)
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");

        // Ligação ao FireBase para o Login
        FirebaseAuth mAuth = ConfiguracaoFireBase.getFirebaseAuth();

        //Configurações Iniciais
        EditText textEmail = findViewById(R.id.editTextEmail);
        EditText textPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        ImageButton imagePassword = findViewById(R.id.imageButtonPasswordLogin);

        // Verificação das Credenciais, Login do Utilizador, e passagem da MainActivity para a HomeActivity (Isto, se as Credenciais estiverem Corretas)
        Configs.botaoLogin(buttonLogin, textEmail, textPassword, this, mAuth);

        // Esconder Mostrar Password
        Configs.esconderMostrarPassword(imagePassword, textPassword);

        Configs.verificarUtilizador(mAuth, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Definição do Menu com 3 pontos no canto superior direto
        Configs.definicaoMenuMain(menu, this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Configuração do Menu com 3 pontos e das suas opções
        Configs.configuracaoMenuLogin(item, this);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}