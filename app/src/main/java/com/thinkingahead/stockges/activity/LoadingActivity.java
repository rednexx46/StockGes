package com.thinkingahead.stockges.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;

import java.util.Objects;
import java.util.Timer;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        // Configurações Iniciais
        ImageView chicodaTau = findViewById(R.id.imageLogo);
        TextView thinkingAhead = findViewById(R.id.textCompany);
        TextView textFooter = findViewById(R.id.textFooter);
        ProgressBar progressBar = findViewById(R.id.progressInicial);

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "Para usar o StockGes é preciso acesso à Internet!", 3000).show();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            finish();
                            onStop();
                            onDestroy();
                        }
                    },
                    3000
            );

        }

        // Esconder Barra Superior (ActionBar)
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Configuração das Animações da , do  e por fim, do nome da Empresa (ThinkingAhead)
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splashtransition);

        // Inicializar as Animações
        chicodaTau.startAnimation(animation); // Imagem "Chico da Tau"
        thinkingAhead.startAnimation(animation); // Nome da Empresa (ThinkingAhead)
        progressBar.startAnimation(animation);
        textFooter.startAnimation(animation);

        // Configuração do Carregamento Inicial
        Configs.configuracaoDoLoadingInicial(this, new Intent(getApplicationContext(), LoginActivity.class));

        // Definir o ErroFrase como Verdadeiro para Dizer ao Utilizador que uma Frase tem de Adicionar (Apenas 1 vez)
        Configs.setErroFrase(true);

    }


    public boolean isConnected() {
        boolean connected = false;

        try {

            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;

        } catch (Exception e) {

            Log.e("Connectivity Exception", e.getMessage());

        }

        return connected;
    }

}