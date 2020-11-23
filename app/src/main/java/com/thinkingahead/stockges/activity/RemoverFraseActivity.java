package com.thinkingahead.stockges.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.adapter.AdapterRecyclerFrases;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.config.ConfiguracaoFireBase;
import com.thinkingahead.stockges.model.Frases;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RemoverFraseActivity extends AppCompatActivity {

    public static List<Frases> listaFrases = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseUser utilizador;
    private TextView textFrase;
    private AdapterRecyclerFrases adapterRecyclerFrases;
    private RelativeLayout carregamentoFrases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remover_frase);

        // Definir Título da Barra Superior (ActionBar)
        Objects.requireNonNull(getSupportActionBar()).setTitle("Remover Frase");

        //Configurações Iniciais
        RecyclerView recyclerView = findViewById(R.id.recyclerFrases);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        utilizador = ConfiguracaoFireBase.getFirebaseAuth().getCurrentUser();
        SwipeRefreshLayout swipeRefreshLayoutFrases = findViewById(R.id.swipeRefreshLayoutFrases);
        textFrase = findViewById(R.id.textFraseRemover);
        carregamentoFrases = findViewById(R.id.carregamentoFrases);

        // Configurar Adapter
        adapterRecyclerFrases = new AdapterRecyclerFrases(listaFrases);

        // Recuperar Frases da Base de Dados
        Configs.recuperarFrases(databaseReference, utilizador, carregamentoFrases, adapterRecyclerFrases, textFrase);

        // Configurar RecyclerView
        Configs.configuracaoRecyclerViewFrases(recyclerView, adapterRecyclerFrases, this, utilizador, textFrase, carregamentoFrases);

        // Configurar SwipeRefreshLayoutFrases para quando o utilizador deslizar o dedo para baixo, haver uma atualização das frases
        Configs.swipeRefreshLayoutFrases(swipeRefreshLayoutFrases, databaseReference, utilizador, adapterRecyclerFrases);


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Recuperar Frases da Base de Dados
        Configs.recuperarFrases(databaseReference, utilizador, carregamentoFrases, adapterRecyclerFrases, textFrase);

    }
}