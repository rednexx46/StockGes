package com.thinkingahead.stockges.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.config.ConfiguracaoFireBase;

public class HomeActivity extends AppCompatActivity {

    // a static variable to get a reference of our application context
    private AppBarConfiguration mAppBarConfiguration;
    private TextView textEmail, textUserName;
    private FirebaseAuth mAuth;
    private ImageView imageProfile;
    //Declaration and defination

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Definição da Toolbar (Barra Superior da Aplicação), onde normalmente fica o nome da App
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Definição do DrawerLayout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        // Definição do NavigationView e do Perfil do Utilizador
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        textEmail = headerView.findViewById(R.id.textRednexx);
        textUserName = headerView.findViewById(R.id.textUserName);
        imageProfile = headerView.findViewById(R.id.imageProfile);
        mAuth = ConfiguracaoFireBase.getFirebaseAuth();

        // Configuração do Menu e dos seus respetivosn Componentes
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_inicio,
                R.id.nav_inserir, R.id.nav_remover, R.id.nav_atualizar, R.id.nav_listar, R.id.nav_sobre).setOpenableLayout(drawer)
                .build();

        // Definição do Layout para o qual vai a Aplicação na sua Execução Inicial
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Definição da ActionBar com o NavController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Definição Final de todos os Componentes
        NavigationUI.setupWithNavController(navigationView, navController);

        FirebaseUser utilizador = ConfiguracaoFireBase.getFirebaseAuth().getCurrentUser();

        if (utilizador != null) {

            utilizador.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (!task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(), "Credenciais Expiradas", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            }
                        }
                    });

        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        // Definir o Controlador de Navegação o ficheiro xml "nav_host_fragment"
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Definição do Menu com 3 pontos no canto superior direto
        Configs.definicaoMenuHome(menu, this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Configuração do Menu com 3 pontos e das suas opções
        Configs.configuracaoMenuHome(item, HomeActivity.this);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        super.onResume();

        // Definir UserName, Email e Foto do Perfil
        Configs.definirPerfil(textUserName, textEmail, imageProfile, getApplicationContext());

    }

}
