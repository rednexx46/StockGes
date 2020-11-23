package com.thinkingahead.stockges.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.config.Configs;

public class SobreFragment extends Fragment {

    public SobreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Configurações Iniciais
        View view = inflater.inflate(R.layout.fragment_sobre, container, false);

        // Configurar a Visualização para o Sobre
        View aboutPage = Configs.verificaoDoTema(getContext());

        return Configs.definicaoDoLayoutSobre(aboutPage, view);

    }

}