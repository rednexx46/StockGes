package com.thinkingahead.stockges.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;
import de.hdodenhof.circleimageview.CircleImageView;

public class InserirFragment extends Fragment {

    private TextView textCodigo;
    private CircleImageView imageProduto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Configuração Inicial
        View rootView = inflater.inflate(R.layout.fragment_inserir, container, false);
        textCodigo = rootView.findViewById(R.id.editTextInserirCodigo);
        imageProduto = rootView.findViewById(R.id.imageProdutoInserir);
        TextView textDescricao = rootView.findViewById(R.id.editTextInserirDescricao);
        TextView textQuantidade = rootView.findViewById(R.id.editTextInserirQuantidade);
        ImageButton imagemQrCode = rootView.findViewById(R.id.imageReaderInserir);
        ImageButton imagemCamara = rootView.findViewById(R.id.imageButtonCamaraInserir);
        ImageButton imagemFotografias = rootView.findViewById(R.id.imageButtonFotografiasInserir);
        FloatingActionButton fab = rootView.findViewById(R.id.fab_Inserir);


        // Enviar o Produto para a Base de Dados
        Configs.cliqueDoBotaoFab(fab, textCodigo, textDescricao, textQuantidade, this, imageProduto);

        // Ouvintes para os Botões de Qr Code, Camara e Fotografias
        Configs.abrirLeitorCamaraFotografias(imagemQrCode, imagemCamara, imagemFotografias, this);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Recuperar a Imagem que o Utilizador Escolheu
        Configs.recuperarDadosImagem(requestCode, data, null, imageProduto, this);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Depois de ser Lido o Código, define o mesmo no "textCodigo"
        Configs.inserirCodigoProduto(textCodigo);

    }

}
