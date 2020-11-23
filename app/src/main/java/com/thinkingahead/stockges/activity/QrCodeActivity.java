package com.thinkingahead.stockges.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;

public class QrCodeActivity extends AppCompatActivity {

    private final IntentIntegrator integrator = new IntentIntegrator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        // Configuração e Inicialização do Integrator, para a leitura do Código de Barras
        Configs.scanCode(integrator, this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Ato de Leitura de Código de Barras e levar o resultado para o "textCodigo"
        Configs.lerCodigoDeBarras(requestCode, resultCode, data, this, integrator);

    }

}