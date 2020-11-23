package com.thinkingahead.stockges.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.config.ConfiguracaoFireBase;

public class InicioFragment extends Fragment {

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Configurações Iniciais
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        TextView textIntro = view.findViewById(R.id.textIntro);
        TextView textFrase = view.findViewById(R.id.textFraseRemover);
        Configs.setProdutoImagem(false);

        // Configurar a Imagem para que cada vez que a Aplicação seja Inicializada, uma Imagem aleatória será mostrada
        ImageView imageViewRandom = view.findViewById(R.id.imageRandom);
        Picasso.get().load("https://picsum.photos/800/600").into(imageViewRandom);

        Configs.definirNomeUtilizador(textIntro);

        // Vamos buscar uma Frase aleatória ao FireBase cada vez que o "Início" é apresentado e de seguida removemos o EventListener
        DatabaseReference databaseReference = ConfiguracaoFireBase.getDataBaseReference().child(Configs.getUserID());
        ValueEventListener valueEventListener = Configs.fraseInspiradora(databaseReference, textFrase, getContext());
        databaseReference.removeEventListener(valueEventListener);

        return view;
    }

}