package com.thinkingahead.stockges.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.config.ConfiguracaoFireBase;
import com.thinkingahead.stockges.helper.Permissao;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.Objects;

public class DefinicoesContaActivity extends AppCompatActivity {

    private CircleImageView imageFoto;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_FOTOGRAFIAS = 200;
    private final String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private final StorageReference storageReference = ConfiguracaoFireBase.getStorageReference();
    private final FirebaseAuth mAuth = ConfiguracaoFireBase.getFirebaseAuth();
    private FirebaseUser utilizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes_conta);

        // Definir Título da Barra Superior (ActionBar)
        Objects.requireNonNull(getSupportActionBar()).setTitle("Conta");

        // Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        // Configurações Iniciais
        imageFoto = findViewById(R.id.imageFotoPerfilConta);
        ImageButton imageFotografias = findViewById(R.id.imageButtonFotografias);
        ImageButton imageCamara = findViewById(R.id.imageButtonCamara);
        ImageButton imageNome = findViewById(R.id.imageButtonAlterarNome);
        TextView textRemoverFrase = findViewById(R.id.textRemoverConta);
        final EditText textNome = findViewById(R.id.editTextNomePerfil);
        utilizador = mAuth.getCurrentUser();
        textNome.setText(Objects.requireNonNull(utilizador).getDisplayName());

        // Configurar Foto de Perfil
        Configs.getProfilePhotoUrl(getApplicationContext(), imageFoto);

        // Definir os Ouvintes para os Cliques nos Botões de adicionar fotografias, abrir a câmara e mudar o nome
        Configs.cliqueImagensNomeFotografiaCamaraConta(this, imageCamara, imageFotografias, imageNome, textRemoverFrase, SELECAO_FOTOGRAFIAS, SELECAO_CAMERA, textNome, utilizador);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Configurar as ações após o clique e o retiro da imagem tanto da camara como da galeria de imagens
        // Fazer o upload da mesma para o Firebase e atualizar o link dela, de seguida definir o CircleImageView
        Configs.resultadoDaActivity(requestCode, data, null, this, imageFoto, utilizador, storageReference);

    }

}

