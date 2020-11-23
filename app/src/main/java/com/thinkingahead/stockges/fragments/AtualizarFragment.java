package com.thinkingahead.stockges.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.adapter.AdapterRecyclerProdutos;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.config.ConfiguracaoFireBase;

public class AtualizarFragment extends Fragment {

    private View rootView;
    private AdapterRecyclerProdutos adapterRecyclerProdutos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ValueEventListener valueEventListenerProdutos;
    private DatabaseReference produtosRef;
    private TextView textStatus;

    public AtualizarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Configurações Iniciais
        rootView = inflater.inflate(R.layout.fragment_listar, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewProdutos);
        produtosRef = ConfiguracaoFireBase.getDataBaseReference().child(Configs.userID).child("produtos");
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayoutProdutos);
        textStatus = rootView.findViewById(R.id.textStatus);
        adapterRecyclerProdutos = new AdapterRecyclerProdutos(ListarFragment.listaProdutos);

        // Configuração e Listagem de Produtos no RecyclerView e SwipeRefreshLayout
        Configs.configuracaoRecyclerViewProdutos(recyclerView, adapterRecyclerProdutos, getContext());
        Configs.recyclerAtualizar(getContext(), recyclerView, adapterRecyclerProdutos);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Configuração do SwipeRefreshLayout e da atualização dos Produtos ao usar o mesmo
        Configs.refreshRecyclerProdutos(getContext(), swipeRefreshLayout, adapterRecyclerProdutos, produtosRef, rootView, textStatus);

        // Configurar TextStatus e o Seu Valor
        Configs.recyclerStatus(textStatus);

        // Recuperar Produtos do Firebase e atribuir o mesmo a um ValueEventListener, para mais tarde o Remover
        valueEventListenerProdutos = Configs.recuperarProdutos(valueEventListenerProdutos, produtosRef, ListarFragment.listaProdutos, rootView, adapterRecyclerProdutos, getContext(), textStatus);

    }

    @Override
    public void onStop() {
        super.onStop();

        // Remover o ValueEventListener
        Configs.removerValueEventListener(produtosRef, valueEventListenerProdutos, getContext());

    }
}
