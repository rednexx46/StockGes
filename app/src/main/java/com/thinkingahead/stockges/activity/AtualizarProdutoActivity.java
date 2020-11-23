package com.thinkingahead.stockges.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.model.Produtos;

import java.util.Objects;

import static com.thinkingahead.stockges.fragments.ListarFragment.listaProdutos;

public class AtualizarProdutoActivity extends AppCompatActivity {

    private TextView textCodigo;
    private Produtos produto;
    private ImageView imageProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar);

        // Definir Título para a Barra Superior (ActionBar)
        Objects.requireNonNull(getSupportActionBar()).setTitle("Atualizar Produto");

        // Ativar Botão de Voltar para Trás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configurações Iniciais
        textCodigo = findViewById(R.id.editTextAtualizarCodigo);
        TextView textQuantidade = findViewById(R.id.editTextAtualizarQuantidade);
        TextView textDescricao = findViewById(R.id.editTextAtualizarDescricao);
        ImageButton imageReader = findViewById(R.id.imageReaderAtualizar);
        ImageButton imagemCamara = findViewById(R.id.imageButtonCamaraAtualizar);
        ImageButton imagemFotografias = findViewById(R.id.imageButtonFotografiasAtualizar);
        FloatingActionButton fab = findViewById(R.id.fab_Atualizar);
        imageProduto = findViewById(R.id.productImageAtualizar);
        Configs.setProdutoImagem(false);

        // Recuperar dados Enviados
        Bundle dados = getIntent().getExtras();
        int position = dados.getInt("posicao");
        produto = listaProdutos.get(position);

        // Definição única do Código do Produto com base no Qr Code e no produto, visto que inicialmente o valor do código é o valor que se encontra na base de dados
        Configs.definirCamposAtualizar(textQuantidade, textDescricao, imageProduto, produto);

        // Configurar Ouvintes Para o QR Code Reader, a Câmara e por fim as Fotografias
        Configs.abrirLeitorCamaraFotografias(imageReader, imagemCamara, imagemFotografias, this);

        // Atualização do Produto na Base de Dados e Definição das Funções do FAB e do ImageButton da Câmara (Qr Code)
        Configs.atualizarProduto(fab, position, textCodigo, textDescricao, textQuantidade, imageProduto, AtualizarProdutoActivity.this);

    }

    @Override
    public void onResume() {
        super.onResume();

        Configs.definirCodigoQr(textCodigo, produto, getApplicationContext());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Configs.recuperarDadosImagem(requestCode, data, null, imageProduto, this);

    }
}
