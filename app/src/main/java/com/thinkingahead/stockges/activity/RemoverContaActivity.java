package com.thinkingahead.stockges.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.config.ConfiguracaoFireBase;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.Objects;

public class RemoverContaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remover_conta);

        // Definir Título da Barra Superior (ActionBar)
        Objects.requireNonNull(getSupportActionBar()).setTitle("Remover Conta");

        // Configurações Iniciais
        Button buttonRemoverConta = findViewById(R.id.buttonConfirmarRemoverConta);
        EditText editPassword = findViewById(R.id.editTextPasswordRemoverConta);
        final FirebaseAuth mAuth = ConfiguracaoFireBase.getFirebaseAuth();
        final FirebaseUser utilizador = mAuth.getCurrentUser();
        ImageButton imageMostrarPassword = findViewById(R.id.imageButtonMostrarPasswordRemover);
        CircleImageView imagePerfil = findViewById(R.id.imagePerfilRemoverConta);

        // Definir Imagem Como Foto de Perfil do Utilizador
        Configs.getProfilePhotoUrl(getApplicationContext(), imagePerfil);


        // Configurar o Botão "Confirmar" e Remover por sí mesmo a Conta, se a password estiver correta
        Configs.confirmarRemoverConta(buttonRemoverConta, editPassword, utilizador, mAuth, this);

        // Mostrar ou Esconder Password
        Configs.esconderMostrarPassword(imageMostrarPassword, editPassword);

    }
}